package cn.easttrans.reaiot.agentic.domain.dto.beamconstruction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

import static cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.DtoConstants.DATE_PATTERN;
import static cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.DtoConstants.SHANGHAI;

public record SwSnLqRequest(
        String deptId,
        int dataType,
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(pattern = DATE_PATTERN, timezone = SHANGHAI)
        LocalDate startTime,
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(pattern = DATE_PATTERN, timezone = SHANGHAI)
        LocalDate endTime,
        String secId,
        String type
) {
}
