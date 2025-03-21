package cn.easttrans.reaiot.domain.dto.beamconstruction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
        Date storageTime, // 入库时间
//        long storageStaffId,
        String storageStaffName, // 入库人姓名
        String remark,
        String deptId // 项目编号
) implements Serializable {
}
