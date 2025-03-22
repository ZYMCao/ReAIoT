package cn.easttrans.reaiot.service.beamconstruction;

import cn.easttrans.reaiot.domain.dto.beamconstruction.LoginRequest;
import cn.easttrans.reaiot.domain.dto.beamconstruction.LoginResponse;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static cn.easttrans.reaiot.EnvironmentalConstants.BEAM.BASE_URL_ENV;

@Service
@Slf4j
public class SysLoginService extends AbstractBeamConstructionService {
    private final WebClient httpClient;
    private final LoginRequest loginRequest;
    private final AtomicReference<Mono<String>> tokenRequestRef = new AtomicReference<>();
    private static final String AUTH_TOKEN = "authToken";

    @Autowired
    protected SysLoginService(@Value(BASE_URL_ENV) String baseUrl,
                              Cache<String, String> cache,
                              WebClient webClient,
                              LoginRequest loginRequest) {
        super(baseUrl, cache);
        this.httpClient = webClient;
        this.loginRequest = loginRequest;
        this.getToken().subscribe(token -> log.debug("Token obtained from {}{} is: {}", baseUrl, LOGIN, token));
    }

    public Mono<LoginResponse> login(LoginRequest loginRequest) {
        return httpClient.post()
                .uri(LOGIN)
                .bodyValue(loginRequest)
                .retrieve()
                .bodyToMono(LoginResponse.class);
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
