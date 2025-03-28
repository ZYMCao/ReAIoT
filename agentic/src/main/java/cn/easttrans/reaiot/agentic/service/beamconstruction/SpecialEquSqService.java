package cn.easttrans.reaiot.agentic.service.beamconstruction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.BEAM.BASE_URL_ENV;

/**
 * 特种设备
 */
@Service
@Slf4j
public class SpecialEquSqService extends AbstractBeamConstructionService {

    public SpecialEquSqService(@Value(BASE_URL_ENV) String baseUrl,
                               Cache<String, String> cache,
                               WebClient webClient,
                               ObjectMapper objectMapper) {
        super(baseUrl, cache, webClient, objectMapper);
    }

}
