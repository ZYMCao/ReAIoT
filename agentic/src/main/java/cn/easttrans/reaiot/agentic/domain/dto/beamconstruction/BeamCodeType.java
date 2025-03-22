package cn.easttrans.reaiot.agentic.domain.dto.beamconstruction;

import java.io.Serial;
import java.io.Serializable;

public record BeamCodeType(
        String id,
        String codeTypeName,
        String description,
        long sortNo,
        String value,
        BeamCodeValue[] codeValueList
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
