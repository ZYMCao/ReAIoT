package cn.easttrans.reaiot.domain.dto;

public record CMPSegment(
        int partId,
        double pDeep,
        double pPulp,
        int pTime,
        int runStatus
) {
}
