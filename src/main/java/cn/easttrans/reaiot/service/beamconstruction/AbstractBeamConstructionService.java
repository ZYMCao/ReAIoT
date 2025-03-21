package cn.easttrans.reaiot.service.beamconstruction;

/**
 * 抽象梁场服务
 */
public abstract class AbstractBeamConstructionService {
    protected final String baseUrl;

    protected AbstractBeamConstructionService(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected static final String LOGIN = "/login";
    protected static final String SES_COUNT_WARN_NUM = "/yzsq/zhgdSpecialEquSq/countWarnNum";
}
