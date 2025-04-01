package cn.easttrans.reaiot.agentic.service.beamconstruction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

/**
 * 抽象梁场服务
 */
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractBeamConstructionService {
    protected final String baseUrl;
    protected final Cache<String, String> cache;
    protected final WebClient httpClient;
    protected final ObjectMapper objectMapper;

    protected static final String LOGIN = "/login";
    protected static final String BEAM_MATERIAL_STORAGE = "/beamMtl/mtlStoragePage";
    protected static final String CODE_TREE = "/beamCode/codeTree";
    protected static final String ASPHALT_CEMENT = "/zhgdAccountMap/zhgdSwSnLq";


    private Mono<JsonNode> checkForErrors(JsonNode json) {
        if (json.has("code") && json.get("code").asInt() != OK.value()) {
            log.error("Request made to {} failed due to {}", baseUrl, json);
            return Mono.error(new ErrorResponseException(BAD_REQUEST));
        }
        return Mono.just(json);
    }

    protected <T> Mono<T> parseJsonResponse(JsonNode json, Class<T> clazz) {
        return checkForErrors(json).flatMap(checkedJson -> {
            try {
                return Mono.just(objectMapper.treeToValue(checkedJson, clazz));
            } catch (IOException e) {
                log.error("Failed to parse {} to {}", checkedJson, clazz.getName(), e);
                return Mono.error(e);
            }
        });
    }

    protected <T> Mono<T> parseJsonResponse(JsonNode json, TypeReference<T> typeRef) {
        return checkForErrors(json).flatMap(checkedJson -> {
            try {
                return Mono.just(objectMapper.readerFor(typeRef).readValue(checkedJson));
            } catch (IOException e) {
                log.error("Failed to parse {} to {}", checkedJson, typeRef.getType().getTypeName(), e);
                return Mono.error(e);
            }
        });
    }
}