package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 16:21
 **/
@Table(ASSET_TABLE_NAME)
public record AssetEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(ADDITIONAL_INFO) String additionalInfo,
        @Column(CUSTOMER_ID) UUID customerId,
        @Column(ASSET_PROFILE_ID) UUID assetProfileId,
        @Column(NAME) String name,
        @Column(LABEL) String label,
        @Column(TENANT_ID) UUID tenantId,
        @Column(TYPE) String type,
        @Column(EXTERNAL_ID) UUID externalId
) {}
