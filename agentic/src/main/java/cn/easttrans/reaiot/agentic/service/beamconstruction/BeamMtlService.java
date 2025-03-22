package cn.easttrans.reaiot.agentic.service.beamconstruction;

import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.BeamCodeType;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.BeamCodeValue;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.MtlStorage;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.MtlStoragePageRequest;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.Page;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.BEAM.BASE_URL_ENV;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@Slf4j
public class BeamMtlService extends AbstractBeamConstructionService {
    private final SysLoginService sysLoginService;

    public BeamMtlService(@Value(BASE_URL_ENV) String baseUrl,
                          Cache<String, String> cache,
                          WebClient webClient,
                          ObjectMapper objectMapper,
                          SysLoginService sysLoginService) {
        super(baseUrl, cache, webClient, objectMapper);
        this.sysLoginService = sysLoginService;
        this.codeTree().subscribe();
    }

    public Mono<MtlStorage[]> mtlStoragePage(MtlStoragePageRequest request) {
        return sysLoginService.getToken()
                .flatMap(token -> this.mtlStoragePage(request, token))
                .map(Result::data)
                .map(Page::records);
    }

    /**
     * /beamMtl/mtlStoragePage
     */
    private Mono<Result<Page<MtlStorage>>> mtlStoragePage(MtlStoragePageRequest request, String token) {
        return httpClient.post()
                .uri(BEAM_MATERIAL_STORAGE)
                .header(AUTHORIZATION, token)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnError(IOException.class, e -> log.error("Error occurred while parsing JSON response from {}{}: {}", baseUrl, BEAM_MATERIAL_STORAGE, e.getMessage()))
                .doOnNext(json -> log.debug("Parsing json received from {}{} ...", baseUrl, BEAM_MATERIAL_STORAGE))
                .flatMap(json -> this.parseJsonResponse(json, new TypeReference<Result<Page<MtlStorage>>>() {}))
                .onErrorResume(e -> {
                    log.error("Fail to fetch and parse data from {}{}: ", baseUrl, BEAM_MATERIAL_STORAGE, e);
                    return Mono.just(new Result<>(e.getMessage(), INTERNAL_SERVER_ERROR.value(), null));
                });
    }

    private Mono<BeamCodeType[]> validateMtlStoragePageRequest(MtlStoragePageRequest request) {
         return sysLoginService.getToken()
                .flatMap(token -> this.codeTree(new LinkedMultiValueMap<>(Map.of("types", List.of("WLMC"))), token))
                .map(Result::data)
                .doOnNext(this::logBeamCodeTypes);
    }

    public Mono<BeamCodeType[]> codeTree() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>(
                Map.of("types", List.of("WLMC", "GGXH")) // 物料名称，规格型号
        );
        return sysLoginService.getToken()
                .flatMap(token -> this.codeTree(queryParams, token))
                .map(Result::data)
                .doOnNext(this::logBeamCodeTypes);
    }

    /**
     * 打印 BeamCodeTypes
     */
    private void logBeamCodeTypes(BeamCodeType[] beamCodeTypes) {
        for (BeamCodeType beamCodeType : beamCodeTypes) {
            log.debug("{} obtained from {}{} is: ", beamCodeType.codeTypeName(), baseUrl, BEAM_MATERIAL_STORAGE);
            for (BeamCodeValue codeValue : beamCodeType.codeValueList()) {
                log.debug("{'codeValue': '{}', 'codeName': '{}'}", codeValue.codeValue(), codeValue.codeName());
            }
        }
    }

    /**
     * /beamCode/codeTree
     */
    private Mono<Result<BeamCodeType[]>> codeTree(MultiValueMap<String, String> queryParams, String token) {
        return httpClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CODE_TREE)
                        .queryParams(queryParams)
                        .build())
                .header(AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Result<BeamCodeType[]>>() {
                })
                .onErrorResume(e -> {
                    log.error("Fail to fetch and parse data from {}{}: ", baseUrl, CODE_TREE, e);
                    return Mono.just(new Result<>(e.getMessage(), INTERNAL_SERVER_ERROR.value(), null));
                });
    }
}
