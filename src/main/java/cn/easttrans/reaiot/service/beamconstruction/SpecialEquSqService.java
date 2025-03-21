package cn.easttrans.reaiot.service.beamconstruction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import static cn.easttrans.reaiot.EnvironmentalConstants.Beam.BASE_URL_ENV;

public class SpecialEquSqService extends AbstractBeamConstructionService {
    private final WebClient httpClient;
    protected SpecialEquSqService(@Value(BASE_URL_ENV) String baseUrl, WebClient webClient) {
        super(baseUrl);
        this.httpClient = webClient;
    }

}
