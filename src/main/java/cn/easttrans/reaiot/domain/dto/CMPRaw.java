package cn.easttrans.reaiot.domain.dto;

import cn.easttrans.reaiot.domain.CMPDataType;
import cn.easttrans.reaiot.domain.CMPWorkStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public record CMPRaw(
        Integer ERR,
        Double emptyMixingDepth,
        @JsonDeserialize(using = CMPDataType.Deserializer.class)
        CMPDataType datatype,
        String shebeino,
        String pileSection,
        String pileno,
        Double deepth,
        Double deg1,
        Double deg2,
        Double speed,
        Double press,
        Double verticality,
        Double grouting,
        @JsonDeserialize(using = CMPWorkStatus.Deserializer.class)
        CMPWorkStatus worksta,
        Double flowmeter,
        Double latitude,
        Double longitude,
        Integer redrivinaFlag,
        String uptime,
        Integer axleType,
        Double initialAgitation,
        Double pileDiam,
        Integer pileBottomTime,
        Double waterCementRatio,
        Double elec,
        Double elec1,
        Double elec2,
        Double effectiveDep,
        Double avgDownSpeed,
        Double avgUpSpeed,
        String startTime,
        String endTime,
        CMPSegment[] segmentData
) {
}

