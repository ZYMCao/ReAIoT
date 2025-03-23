package cn.easttrans.reaiot.agentic.domain.dto.beamconstruction;

import java.io.Serializable;

public record MtlStoragePageRequest(
        int pageNum,
        int pageSize,
        String mtlName, // 物料名称
        String specificationMode, // 规格型号
        String deptId //项目号
) implements Serializable {
    public MtlStoragePageRequest(int pageSize) {
        this(1, pageSize, "", "", "");
    }

    public MtlStoragePageRequest(int pageSize, String mtlName) {
        this(1, pageSize, mtlName, "", "");
    }
}
