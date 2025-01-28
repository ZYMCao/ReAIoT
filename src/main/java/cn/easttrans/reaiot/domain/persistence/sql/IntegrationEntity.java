package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;


/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 17:29
 **/
@Table(INTEGRATION_TABLE_NAME)
public record IntegrationEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(ADDITIONAL_INFO) String additionalInfo,
        @Column(CONFIGURATION) String configuration,
        @Column(DEBUG_MODE) boolean debugMode,
        @Column(ENABLED) boolean enabled,
        @Column(IS_REMOTE) boolean isRemote,
        @Column(ALLOW_CREATE_DEVICES_OR_ASSETS) boolean allowCreateDevicesOrAssets,
        @Column(NAME) String name,
        @Column(SECRET) String secret,
        @Column(CONVERTER_ID) UUID converterId,
        @Column(DOWNLINK_CONVERTER_ID) UUID downlinkConverterId,
        @Column(ROUTING_KEY) String routingKey,
        @Column(TENANT_ID) UUID tenantId,
        @Column(TYPE) String type,
        @Column(EXTERNAL_ID) UUID externalId,
        @Column(IS_EDGE_TEMPLATE) boolean isEdgeTemplate
) {}
