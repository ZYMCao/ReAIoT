package cn.easttrans.reaiot.agentic.service.beamconstruction;

import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.LoginRequest;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.LoginResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.BEAM.BASE_URL_ENV;

@Service
@Slf4j
public class SysLoginService extends AbstractBeamConstructionService {
    private final LoginRequest loginRequest;
    private final AtomicReference<Mono<String>> tokenRequestRef = new AtomicReference<>();
    private static final String AUTH_TOKEN = "authToken";

    @Autowired
    protected SysLoginService(@Value(BASE_URL_ENV) String baseUrl,
                              Cache<String, String> cache,
                              WebClient webClient,
                              ObjectMapper objectMapper,
                              LoginRequest loginRequest) {
        super(baseUrl, cache, webClient, objectMapper);
        this.loginRequest = loginRequest;
        this.getToken().subscribe();
    }

    public Mono<LoginResponse> login(LoginRequest loginRequest) {
        return httpClient.post()
                .uri(LOGIN)
                .bodyValue(loginRequest)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnError(IOException.class, e -> log.error("Error occurred while parsing JSON response from {}{}: {}", baseUrl, LOGIN, e.getMessage()))
                .doOnNext(json -> log.debug("Parsing json received from {}{} ...", baseUrl, LOGIN))
                .flatMap(json -> this.parseJsonResponse(json, LoginResponse.class))
                .doOnNext(res -> log.debug("Token obtained from {}{} is: {}", baseUrl, LOGIN, res.token()));
    }

    public Mono<String> getToken() {
        return getToken(this.loginRequest);
    }

    private Mono<String> getToken(LoginRequest loginRequest) {
        return Mono.defer(() -> Mono
                .justOrEmpty(cache.getIfPresent(AUTH_TOKEN))
                .switchIfEmpty(tokenRequestRef.updateAndGet(current -> current == null ? createNewTokenRequest(loginRequest) : current)));
    }

    private Mono<String> createNewTokenRequest(LoginRequest loginRequest) {
        return login(loginRequest)
                .map(LoginResponse::token)
                .doOnSuccess(token -> cache.put(AUTH_TOKEN, token))
                .doFinally(__ -> tokenRequestRef.set(null))
                .cache();
    }
}
