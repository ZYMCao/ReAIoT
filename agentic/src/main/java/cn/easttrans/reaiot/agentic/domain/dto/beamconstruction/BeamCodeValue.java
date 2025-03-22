package cn.easttrans.reaiot.agentic.domain.dto.beamconstruction;

import java.io.Serial;
import java.io.Serializable;

public record BeamCodeValue(
        String id,
        String pCodeType,  // 父码值类型标识
        String pCodeValue, // 父码值
        String codeTypeId, // 码值类型标识
        String codeValue, // 码值
        String codeName, // 码值名称
        String unit, // 单位
        long sortNo, // 序号
        BeamCodeValue[] childrenList
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
