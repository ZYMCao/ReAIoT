package cn.easttrans.reaiot.domain.dto.beamconstruction;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record BeamCodeType(
        String id,
        String codeTypeName,
        String description,
        long sortNo,
        String value,
        List<BeamCodeValue> codeValueList
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
