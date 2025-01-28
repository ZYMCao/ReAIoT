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
@Table(ASSET_PROFILE_TABLE_NAME)
public record AssetProfileEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(NAME) String name,
        @Column(IMAGE) String image,
        @Column(DESCRIPTION) String description,
        @Column(IS_DEFAULT) boolean isDefault,
        @Column(TENANT_ID) UUID tenantId,
        @Column(DEFAULT_RULE_CHAIN_ID) UUID defaultRuleChainId,
        @Column(DEFAULT_DASHBOARD_ID) UUID defaultDashboardId,
        @Column(DEFAULT_QUEUE_NAME) String defaultQueueName,
        @Column(DEFAULT_EDGE_RULE_CHAIN_ID) UUID defaultEdgeRuleChainId,
        @Column(EXTERNAL_ID) UUID externalId
) {
}
