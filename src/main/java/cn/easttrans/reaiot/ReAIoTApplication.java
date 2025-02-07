package cn.easttrans.reaiot;

import cn.easttrans.reaiot.mqtt.DefaultMqttClient;
import cn.easttrans.reaiot.mqtt.MqttClient;
import cn.easttrans.reaiot.mqtt.MqttClientConfig;
import cn.easttrans.reaiot.mqtt.ReMqttClient;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.reactor.MqttReactorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
public class ReAIoTApplication {
    private static final String SPRING_CONFIG_NAME_KEY = "--spring.config.name";
    private static final String DEFAULT_SPRING_CONFIG_PARAM = SPRING_CONFIG_NAME_KEY + "=" + "ReAIoT";

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
        SpringApplication.run(ReAIoTApplication.class, updateArguments(args));
        testHive();
    }

    private static void testHive() {
        MqttClientConfig localConfig = new MqttClientConfig("ReAIoT", "2.7182818e");
        DefaultMqttClient defaultMqttClient = new DefaultMqttClient(localConfig);
        var connection = defaultMqttClient.connect();
    }

    private static void testClient() {
        MqttClientConfig localConfig = new MqttClientConfig("ReAIoT", "2.7182818e");
        DefaultMqttClient defaultMqttClient = new DefaultMqttClient(localConfig);
        var connection = defaultMqttClient.connect();

        connection.inbound()
                .receive()
                .doOnNext(byteBuf ->
                        log.info(byteBuf.toString())
                )
//                .doOnNext(byteBuf -> log.info("Received message: {}", byteBuf.toString(StandardCharsets.UTF_8)))
                .then()
                .subscribe();

//        connection.onDispose()
//                .block();
    }

    private static void testReactiveClient() {
        MqttClientConfig localConfig = new MqttClientConfig("ReAIoT", "2.7182818e");
        ReMqttClient reactorMqttClient = new ReMqttClient(localConfig);
        reactorMqttClient.connect().subscribe();
//        reactorMqttClient.connect("iot.djzhgd.com", 10006).subscribe();
    }
}
