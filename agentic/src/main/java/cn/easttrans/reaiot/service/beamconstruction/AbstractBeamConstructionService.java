package cn.easttrans.reaiot.service.beamconstruction;

import com.github.benmanes.caffeine.cache.Cache;

/**
 * 抽象梁场服务
 */
public abstract class AbstractBeamConstructionService {
    protected final String baseUrl;
    protected final Cache<String, String> cache;

    protected AbstractBeamConstructionService(final String baseUrl, final Cache<String, String> cache) {
        this.baseUrl = baseUrl;
        this.cache = cache;
    }

    protected static final String LOGIN = "/login";
    protected static final String BEAM_MATERIAL_STORAGE = "/beamMtl/mtlStoragePage";
}
