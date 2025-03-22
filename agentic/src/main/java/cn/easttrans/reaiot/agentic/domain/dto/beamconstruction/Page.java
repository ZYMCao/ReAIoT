package cn.easttrans.reaiot.agentic.domain.dto.beamconstruction;

import java.io.Serializable;

/**
 * com.baomidou.mybatisplus.extension.plugins.pagination.Page
 */
public record Page<T>(
        T[] records,
        long total,
        long size,
        long current
) implements Serializable {
}
