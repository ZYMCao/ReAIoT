package cn.easttrans.reaiot.agentic.service.beamconstruction;

import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.SwSnLqRequest;
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
import java.time.LocalDate;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.BEAM.BASE_URL_ENV;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
public class AccountMapService extends AbstractBeamConstructionService {
    private final SysLoginService sysLoginService;

    @Autowired
    public AccountMapService(@Value(BASE_URL_ENV) String baseUrl,
                             Cache<String, String> cache,
                             WebClient webClient,
                             ObjectMapper objectMapper,
                             SysLoginService sysLoginService) {
        super(baseUrl, cache, webClient, objectMapper);
        this.sysLoginService = sysLoginService;

        var req = new SwSnLqRequest("5853", 2, LocalDate.parse("2024-04-01"), LocalDate.parse("2025-04-01"), "", "GKSN");
        this.zhgdSwSnLq(req)
                .doOnNext(
                        r -> log.info(String.valueOf(r))
                )
                .subscribe();
    }

    public Mono<JsonNode> zhgdSwSnLq(SwSnLqRequest request) {
        return sysLoginService.getToken()
                .flatMap(token -> this.zhgdSwSnLq(request, token));
    }

    /**
     * /zhgdAccountMap/zhgdSwSnLq
     */
    private Mono<JsonNode> zhgdSwSnLq(SwSnLqRequest request, String token) {
        return httpClient.post()
                .uri(ASPHALT_CEMENT)
                .header(AUTHORIZATION, token)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnNext(j -> log.info(String.valueOf(j)))
                .doOnError(IOException.class, e -> log.error("Error occurred while parsing JSON response from {}{}: {}", baseUrl, ASPHALT_CEMENT, e.getMessage()))
                .doOnNext(json -> log.debug("Parsing json received from {}{} ...", baseUrl, ASPHALT_CEMENT));
    }
}
