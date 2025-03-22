package cn.easttrans.reaiot.agentic.service.beamconstruction;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;

/**
 * 抽象梁场服务
 */
@RequiredArgsConstructor
public abstract class AbstractBeamConstructionService {
    protected final String baseUrl;
    protected final Cache<String, String> cache;

    protected static final String LOGIN = "/login";
    protected static final String BEAM_MATERIAL_STORAGE = "/beamMtl/mtlStoragePage";
    protected static final String CODE_TREE = "/beamCode/codeTree";
}
