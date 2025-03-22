package cn.easttrans.reaiot.agentic.service.beamconstruction;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 抽象梁场服务
 */
@RequiredArgsConstructor
public abstract class AbstractBeamConstructionService {
    protected final String baseUrl;
    protected final Cache<String, String> cache;
    protected final WebClient httpClient;

    protected static final String LOGIN = "/login";
    protected static final String BEAM_MATERIAL_STORAGE = "/beamMtl/mtlStoragePage";
    protected static final String CODE_TREE = "/beamCode/codeTree";
}
