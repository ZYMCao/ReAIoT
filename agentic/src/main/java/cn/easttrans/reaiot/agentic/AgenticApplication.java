package cn.easttrans.reaiot.agentic;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.CASSANDRA_CONTACT_ENV;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.util.concurrent.TimeUnit.MINUTES;


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

    @Bean
    public CqlSession cqlSession(@Value(CASSANDRA_CONTACT_ENV) String contactPoint) {
        return new CqlSessionBuilder()
                .addContactPoint(new InetSocketAddress(contactPoint, 9042))
                .withLocalDatacenter("datacenter1")
                .build();
    }

    @Bean
    public EmbeddingModel defaultEmbeddingModel() throws Exception {
        var embeddingModel = new TransformersEmbeddingModel();
        embeddingModel.setTokenizerResource("classpath:/onnx/all-MiniLM-L6-v2/tokenizer.json");
        embeddingModel.setModelResource("classpath:/onnx/all-MiniLM-L6-v2/model.onnx"); // ToDo: Outsource model.onnx
        embeddingModel.setResourceCacheDirectory("/tmp/onnx-zoo");
        embeddingModel.setTokenizerOptions(Map.of("padding", "true"));

        embeddingModel.afterPropertiesSet();
        return embeddingModel;
    }

    @Bean
    public Cache<String, String> tokenCache() {
        return Caffeine.newBuilder().expireAfterWrite(20, MINUTES).maximumSize(1).build();
    }

    @Bean
    public Cache<String, Set<String>> userSessionsCache() {
        return Caffeine.newBuilder().expireAfterWrite(30, MINUTES).maximumSize(1000).build();
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
}