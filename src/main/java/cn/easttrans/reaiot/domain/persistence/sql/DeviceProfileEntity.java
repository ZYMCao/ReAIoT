package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

@Table(DEVICE_PROFILE_TABLE_NAME)
public record DeviceProfileEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(NAME) String name,
        @Column(TYPE) String type,
        @Column(IMAGE) String image,
        @Column(TRANSPORT_TYPE) String transportType,
        @Column(PROVISION_TYPE) String provisionType,
        @Column(PROFILE_DATA) String profileData,
        @Column(DESCRIPTION) String description,
        @Column(IS_DEFAULT) boolean isDefault,
        @Column(TENANT_ID) UUID tenantId,
        @Column(FIRMWARE_ID) UUID firmwareId,
        @Column(SOFTWARE_ID) UUID softwareId,
        @Column(DEFAULT_RULE_CHAIN_ID) UUID defaultRuleChainId,
        @Column(DEFAULT_DASHBOARD_ID) UUID defaultDashboardId,
        @Column(DEFAULT_QUEUE_NAME) String defaultQueueName,
        @Column(PROVISION_DEVICE_KEY) String provisionDeviceKey,
        @Column(DEFAULT_EDGE_RULE_CHAIN_ID) UUID defaultEdgeRuleChainId,
        @Column(EXTERNAL_ID) UUID externalId
) {
}
