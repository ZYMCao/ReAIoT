package cn.easttrans.reaiot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public enum SensorError {
    RESERVED(0, "预留"),
    DEPTH(1, "深度传感器"),
    ELECTRIC_2(2, "电流传感器2号"),
    ELECTRIC_1(3, "电流传感器1号"),
    DEGREE(4, "倾角传感器"),
    FLOW_1(5, "3号流量传感器"),
    FLOW_2(6, "2号流量传感器"),
    FLOW_3(7, "1号流量传感器"),
    ;

    final int bit;
    final String description;

    public static Set<SensorError> fromBitMask(int bit) {
        return EnumSet.allOf(SensorError.class).stream()
                .filter(error -> (bit & (1 << error.getBit())) != 0)
                .collect(Collectors.toSet());
    }

    public static final Comparator<SensorError> COMPARATOR = Comparator.comparingInt(SensorError::getBit);
}
