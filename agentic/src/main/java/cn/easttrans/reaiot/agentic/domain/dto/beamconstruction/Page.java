package cn.easttrans.reaiot.agentic.domain.dto.beamconstruction;

import java.io.Serializable;
import java.util.List;

/**
 * com.baomidou.mybatisplus.extension.plugins.pagination.Page
 */
public record Page<T>(
        List<T> records,
        long total,
        long size,
        long current
) implements Serializable {
}
