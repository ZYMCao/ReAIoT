package cn.easttrans.reaiot.agentic.service.beamconstruction;

import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.BeamCodeType;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.BeamCodeValue;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.MtlStorage;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.MtlStoragePageRequest;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.Page;
import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.Result;
import cn.easttrans.reaiot.agentic.domain.enumeration.beamconstruction.CodeEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.BEAM.BASE_URL_ENV;
import static cn.easttrans.reaiot.agentic.domain.enumeration.beamconstruction.CodeEnum.GGXH;
import static cn.easttrans.reaiot.agentic.domain.enumeration.beamconstruction.CodeEnum.WLMC;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
//        this.materialStorage(new MtlStoragePageRequest(50)).subscribe();
    }

    /**
     * 校验 MtlStoragePageRequest::mtlName 入参的合法性
     */
    private Mono<MtlStoragePageRequest> checkForErrors(MtlStoragePageRequest request) {
        String materialName = request.mtlName();
        if (null == materialName || materialName.isEmpty()) {
            return Mono.just(request);
        }
        log.trace("Request to {}{} is validated: {}", baseUrl, BEAM_MATERIAL_STORAGE, request);
        Mono<String[]> legitMaterialNames = this.codeTree(WLMC).flatMap(beamCodeType ->
                beamCodeType.codeValueList() == null ?
                        Mono.empty() :
                        Mono.just(Arrays.stream(beamCodeType.codeValueList())
                                .map(BeamCodeValue::codeValue)
                                .toArray(String[]::new)));

        return legitMaterialNames.flatMap(mtlNames ->
                Arrays.asList(mtlNames).contains(materialName) ?
                        Mono.just(request) :
                        Mono.error(new IllegalArgumentException("Invalid material name: " + materialName)));
    }

    /**
     * 获取【物料入库管理】的数据
     */
    public Mono<MtlStorage[]> materialStorage(MtlStoragePageRequest request) {
        return checkForErrors(request)
                .flatMap(validatedRequest -> sysLoginService.getToken()
                        .flatMap(token -> this.mtlStoragePage(validatedRequest, token))
                        .map(Result::data)
                        .map(Page::records)
                )
                .doOnNext(this::logMtlStorages);
    }

    //    @Tool(description = "Fetch 100 data of building materials most recently warehoused")
    public MtlStorage[] materialStorage100() {
        MtlStorage[] materials = this.materialStorage(new MtlStoragePageRequest(100)).block();
        log.debug("Tool materialStorage is called: {}", null == materials ? "" : Arrays.toString(materials));
        return materials;
    }

    //    @Tool(description = "Fetch data of building materials most recently warehoused")
    public MtlStorage[] materialStorage(@ToolParam(description = "the amount of rows of data to be fetched") int size) {
        MtlStorage[] materials = this.materialStorage(new MtlStoragePageRequest(size)).block();
        log.debug("Tool materialStorage is called: {}", null == materials ? "" : Arrays.toString(materials));
        return materials;
    }

    //    @Tool(description = "Fetch data of building materials most recently warehoused")
    public MtlStorage[] materialStorageByCode(@ToolParam(description = "integer indicating the amount of rows of data to be fetched") int size,
                                              @ToolParam(description = "codeValue, whose enumerations can be fetched by 'codeMaterial', used to filter materials by their kind") String mtlName) {
        MtlStorage[] materials = this.materialStorage(new MtlStoragePageRequest(size)).block();
        log.debug("Tool materialStorage is called: {}", null == materials ? "" : Arrays.toString(materials));
        return materials;
    }

    /**
     * 打印 MtlStorage[]
     */
    private void logMtlStorages(MtlStorage[] storages) {
        log.debug("物料入库管理的 {} 条数据: ", storages.length);
        for (MtlStorage storage : storages) {
            log.debug(String.valueOf(storage));
        }
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
                .flatMap(json -> this.parseJsonResponse(json, new TypeReference<Result<Page<MtlStorage>>>() {
                }));
    }

    public Mono<BeamCodeType> codeTree(CodeEnum queryValue) {
        return codeTree(new CodeEnum[]{queryValue}).flatMapMany(Flux::fromArray).next();
    }

    public Mono<BeamCodeType[]> codeTree(CodeEnum[] queryValues) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>(Map.of("types",
                Arrays.stream(queryValues).map(CodeEnum::name).collect(Collectors.toList())
        ));
        return sysLoginService.getToken()
                .flatMap(token -> this.codeTree(queryParams, token))
                .map(Result::data)
                .doOnNext(this::logBeamCodeTypes);
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
                .bodyToMono(JsonNode.class)
                .doOnError(IOException.class, e -> log.error("Error occurred while parsing JSON response from {}{}: {}", baseUrl, LOGIN, e.getMessage()))
                .doOnNext(json -> log.debug("Parsing json received from {}{} ...", baseUrl, CODE_TREE))
                .flatMap(json -> this.parseJsonResponse(json, new TypeReference<Result<BeamCodeType[]>>() {
                }));
    }

    //    @Tool(description = "kv pairs where keys are codes and values are their descriptions")
    public Map<String, String> codeMaterial() {
        return codeTree(WLMC)
                .map(BeamCodeType::codeValueList)
                .map(codeValues -> Arrays.stream(codeValues)
                        .collect(Collectors.toMap(
                                BeamCodeValue::codeValue,
                                BeamCodeValue::codeName
                        ))
                )
                .block();
    }


    /**
     * 打印 BeamCodeTypes
     */
    private void logBeamCodeTypes(BeamCodeType[] beamCodeTypes) {
        for (BeamCodeType beamCodeType : beamCodeTypes) {
            log.debug("{} obtained from {}{} is: ", beamCodeType.codeTypeName(), baseUrl, CODE_TREE);
            for (BeamCodeValue codeValue : beamCodeType.codeValueList()) {
                log.debug("{'codeValue': '{}', 'codeName': '{}'}", codeValue.codeValue(), codeValue.codeName());
            }
        }
    }
}
