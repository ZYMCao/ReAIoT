package cn.easttrans.reaiot.agentic;

import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.LoginRequest;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.memory.cassandra.CassandraChatMemory;
import org.springframework.ai.chat.memory.cassandra.CassandraChatMemoryConfig;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.cassandra.CassandraVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.InetSocketAddress;
import java.util.Arrays;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.BEAM.BASE_URL_ENV;
import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.BEAM.PASSWORD_ENV;
import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.BEAM.SUFFIX_ENV;
import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.BEAM.USERNAME_ENV;
import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.CASSANDRA_CONTACT_ENV;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;


@SpringBootApplication
public class AgenticApplication {
    private static final String SPRING_CONFIG_NAME_KEY = "--spring.config.name";
    private static final String DEFAULT_SPRING_CONFIG_PARAM = SPRING_CONFIG_NAME_KEY + "=" + "agentic";

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
        SpringApplication.run(AgenticApplication.class, updateArguments(args));
    }

    //    @Bean
    ChatMemory defaultMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    ChatMemory defaultCassandraMemory(CqlSession cqlSession) {
        return CassandraChatMemory.create(CassandraChatMemoryConfig.builder()
                // .withTimeToLive(Duration.ofDays(30)) // ToDo: 讨论一下需要保存对话记录多久？
                .withCqlSession(cqlSession)
                .build());
    }

    @Bean
    public CqlSession cqlSession(@Value("${easttrans.cassandra.contact-point:}") String contactPoint) {
        return new CqlSessionBuilder()
                .addContactPoint(new InetSocketAddress(contactPoint, 9042))
                .withLocalDatacenter("datacenter1")
                .build();
    }

    @Bean
    public EmbeddingModel defaultEmbeddingModel() {
        // default is ONNX all-MiniLM-L6-v2 which is what we want
        return new TransformersEmbeddingModel();
    }

    @Bean
    public VectorStore defaultVectorStore(CqlSession cqlSession, EmbeddingModel embeddingModel) {
        return CassandraVectorStore.builder(embeddingModel)
                .session(cqlSession)
                .build();
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
    public ObjectMapper defaultObjectMapper() {
        return new ObjectMapper()
                /*
                Fix: JSON decoding error: Cannot coerce empty String ("") to element of `BeamCodeValue[]`
                 */
                .configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                /*
                Fix: org.springframework.core.codec.DecodingException: JSON decoding error: Unrecognized field msg (class LoginResponse)
                 */
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public WebClient defaultHttpClient(@Value(BASE_URL_ENV) String baseUrl, ObjectMapper objectMapper) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .defaultHeader("Projectid", "5853") // ToDo: 写死吗??
                .codecs(configurer -> configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper)))
                .build();
    }
}