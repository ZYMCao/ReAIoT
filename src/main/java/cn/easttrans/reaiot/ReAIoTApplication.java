package cn.easttrans.reaiot;

import cn.easttrans.reaiot.domain.dto.beamconstruction.LoginRequest;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

import static cn.easttrans.reaiot.EnvironmentalConstants.BEAM.*;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;


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
    }

    @Bean
    ChatMemory defaultMemory() {
        return new InMemoryChatMemory(); // ToDo: 替换为 org.springframework.ai.chat.memory.CassandraChatMemory 作持久化
    }

    @Bean
    LoginRequest defaultLoginRequest(@Value(USERNAME_ENV) String username,
                                     @Value(PASSWORD_ENV) String password,
                                     @Value(SUFFIX_ENV) String suffix) {
        return new LoginRequest(username, password, suffix);
    }

    @Bean
    public Cache<String, String> defaultCache() {
        return Caffeine.newBuilder().expireAfterWrite(20, MINUTES).maximumSize(1).build();
    }

    @Bean
    public WebClient defaultHttpClient(@Value(BASE_URL_ENV) String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .defaultHeader("Projectid", "5853") // ToDo: 写死吗??
                .build();
    }
}