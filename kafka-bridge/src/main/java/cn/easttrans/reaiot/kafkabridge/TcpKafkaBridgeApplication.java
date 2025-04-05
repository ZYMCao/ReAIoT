package cn.easttrans.reaiot.kafkabridge;

import io.netty.handler.codec.LineBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;
import reactor.netty.tcp.TcpClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import static java.lang.Long.MAX_VALUE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@SpringBootApplication
@Slf4j
public class TcpKafkaBridgeApplication implements CommandLineRunner {
    private static final String SPRING_CONFIG_NAME_KEY = "--spring.config.name";
    private static final String DEFAULT_SPRING_CONFIG_PARAM = SPRING_CONFIG_NAME_KEY + "=" + "kafkabridge";

    private static String[] updateArguments(String[] args) {
        if (Arrays.stream(args).noneMatch(arg -> arg.startsWith(SPRING_CONFIG_NAME_KEY))) {
            String[] modifiedArgs = new String[args.length + 1];
            System.arraycopy(args, 0, modifiedArgs, 0, args.length);
            modifiedArgs[args.length] = DEFAULT_SPRING_CONFIG_PARAM;
            return modifiedArgs;
        }
        return args;
    }

    public static void main(String[] args) {
        SpringApplication.run(TcpKafkaBridgeApplication.class, updateArguments(args));
    }

    @Bean
    public Disposable startTcpToKafkaPipeline(
            @Value("${beidou.tcp.host}") String host,
            @Value("${beidou.tcp.port}") int port,
            @Value("${beidou.kafka.topic}") String topic,
            KafkaSender<byte[], byte[]> kafkaSender) {
        TcpClient tcpClient = TcpClient.create()
                .host(host)
                .port(port)
                .doOnConnected(conn -> conn.addHandler(new LineBasedFrameDecoder(1024 * 1024)));
        return tcpClient.connect()
                .flatMap(connection -> connection
                        .inbound()
                        .receive()
                        .asByteArray()
                        .map(bytes -> new String(bytes, UTF_8))
                        .filter(line -> !line.isBlank())
                        .doOnNext(line -> log.info("Received: {}", line))
                        .flatMap(line -> sendToKafka(kafkaSender, topic, line))
                        .then()
                        .doFinally(signal -> connection.dispose()))
                .retryWhen(Retry.backoff(MAX_VALUE, Duration.ofSeconds(3)))
                .subscribe();
    }

    private Mono<Void> sendToKafka(KafkaSender<byte[], byte[]> sender, String topic, String line) {
        ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, line.getBytes(UTF_8));
        return sender.send(Mono.just(SenderRecord.create(record, null)))
                .doOnError(e -> log.error("Failed to send message", e))
                .then();
    }

    @Bean
    public KafkaSender<byte[], byte[]> kafkaSender(@Value("${spring.kafka.producer.bootstrap-servers}") String bootstrapServers) {
        SenderOptions<byte[], byte[]> senderOptions = SenderOptions.create(Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class,
                VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class,
                RETRIES_CONFIG, 3,
                ACKS_CONFIG, "all"
        ));
        return KafkaSender.create(senderOptions);
    }

    @Override
    public void run(String... args) {
        Mono.never().block();
    }
}
