package cn.easttrans.reaiot;

import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt3.Mqtt3RxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
@Slf4j
public class ReAIoTApplication {
    private static final String SPRING_CONFIG_NAME_KEY = "--spring.config.name";
    private static final String DEFAULT_SPRING_CONFIG_PARAM = SPRING_CONFIG_NAME_KEY + "=" + "ReAIoT";
    private static final int EASTTRANS_MQTT_PORT = 10006;
    private static final String EASTTRANS_IOT_URL = "103.213.97.54";
    private static final String CLIENT_ID = randomClientId(new Random());

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
        log.info("MQTT Client, {}, is connecting to the MQTT Broker of {}:{}...", CLIENT_ID, EASTTRANS_IOT_URL, EASTTRANS_MQTT_PORT);
    }

    public static String randomClientId(Random random) {
        final String[] options = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split("");
        return "ReAIoT_" + IntStream.range(0, 8)
                .mapToObj(i -> options[random.nextInt(options.length)])
                .collect(Collectors.joining());
    }

    @Bean
    public Mqtt3RxClient mqtt3ReactorClient() {
        return Mqtt3Client.builder()
                .identifier(CLIENT_ID)
                .serverHost(EASTTRANS_IOT_URL)
                .serverPort(EASTTRANS_MQTT_PORT)
                .buildRx();
    }
}