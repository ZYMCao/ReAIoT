package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 17:26
 **/
@Table(EDGE_TABLE_NAME)
public record EdgeEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(ADDITIONAL_INFO) String additionalInfo,
        @Column(CUSTOMER_ID) UUID customerId,
        @Column(ROOT_RULE_CHAIN_ID) UUID rootRuleChainId,
        @Column(TYPE) String type,
        @Column(NAME) String name,
        @Column(LABEL) String label,
        @Column(ROUTING_KEY) String routingKey,
        @Column(SECRET) String secret,
        @Column(EDGE_LICENSE_KEY) String edgeLicenseKey,
        @Column(CLOUD_ENDPOINT) String cloudEndpoint,
        @Column(TENANT_ID) UUID tenantId
) {}
