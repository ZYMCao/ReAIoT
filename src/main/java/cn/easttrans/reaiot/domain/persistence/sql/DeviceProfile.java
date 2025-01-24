package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.persistence.ModelConstants.*;

@Table(DEVICE_PROFILE_TABLE_NAME)
public record DeviceProfile(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        String name,
        String type,
        String image,
        @Column(DEVICE_PROFILE_TRANSPORT_TYPE) String transportType,
        @Column(DEVICE_PROFILE_PROVISION_TYPE) String provisionType,
        @Column(DEVICE_PROFILE_PROFILE_DATA) String profileData,
        String description,
        @Column(DEVICE_PROFILE_IS_DEFAULT) boolean isDefault,
        @Column(TENANT_ID) UUID tenantId,
        @Column(DEVICE_FIRMWARE_ID) UUID firmwareId,
        @Column(DEVICE_SOFTWARE_ID) UUID softwareId,
        @Column(DEFAULT_RULE_CHAIN_ID) UUID defaultRuleChainId,
        @Column(DEFAULT_DASHBOARD_ID) UUID defaultDashboardId,
        @Column(DEFAULT_QUEUE_NAME) String defaultQueueName,
        @Column(PROVISION_DEVICE_KEY) String provisionDeviceKey,
        @Column(DEFAULT_EDGE_RULE_CHAIN_ID) UUID defaultEdgeRuleChainId,
        @Column(EXTERNAL_ID) UUID externalId
) {
}
