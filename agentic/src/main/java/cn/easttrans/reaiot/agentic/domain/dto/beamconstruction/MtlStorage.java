package cn.easttrans.reaiot.agentic.domain.dto.beamconstruction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.DtoConstants.DATE_TIME_PATTERN;
import static cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.DtoConstants.SHANGHAI;

public record MtlStorage(
        String id,
        String batchId, // 批次
//        String mtlName,
        String mtlText, // 物料名称文本
        String specificationMode, // 规格型号
        String specificationText, // 规格型号
        String unit,
        BigDecimal tonnage,
        String vendor, // 供应商
        String carNumber, // 车牌号
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        // Fix: Java 8 date/time type `java.time.LocalDateTime` not supported by default
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        // Fix: Cannot deserialize value of type `java.time.LocalDateTime` from String "2025-03-23 10:45:23"
        @JsonFormat(pattern = DATE_TIME_PATTERN, timezone = SHANGHAI)
        LocalDateTime storageTime, // 入库时间
//        long storageStaffId,
        String storageStaffName // 入库人姓名
//        String remark,
//        String deptId // 项目编号
) implements Serializable {
}
