package cn.easttrans.reaiot.agentic.service.beamconstruction;

import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.MtlStorage;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.MtlStoragePageRequest;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.Page;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.Result;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.BEAM.BASE_URL_ENV;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@Slf4j
public class BeamMtlService extends AbstractBeamConstructionService {
    private final WebClient httpClient;
    private final SysLoginService sysLoginService;

    public BeamMtlService(@Value(BASE_URL_ENV) String baseUrl,
                          Cache<String, String> cache,
                          WebClient webClient,
                          SysLoginService sysLoginService) {
        super(baseUrl, cache);
        this.httpClient = webClient;
        this.sysLoginService = sysLoginService;
    }

    public Mono<Result<Page<MtlStorage>>> mtlStoragePage(MtlStoragePageRequest request, String token) {
        return httpClient.post()
                .uri(BEAM_MATERIAL_STORAGE)
                .header(AUTHORIZATION, token)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Result<Page<MtlStorage>>>() {
                })
                .onErrorResume(e -> {
                    log.error("Fail to fetch and parse data from {}{}: ", baseUrl, BEAM_MATERIAL_STORAGE, e);
                    return Mono.just(new Result<>(e.getMessage(), INTERNAL_SERVER_ERROR.value(), null));
                });
    }

//    public Mono<Result<List<BeamCodeType>>>
}
