package cn.easttrans.reaiot.agentic.domain.dto.beamconstruction;

import java.io.Serial;
import java.io.Serializable;

/**
 * com.djzhgd.framework.web.domain.ResultBean
 */
public record Result<T>(String msg, int code, T data) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
