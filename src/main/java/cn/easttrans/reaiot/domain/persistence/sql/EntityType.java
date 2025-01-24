package cn.easttrans.reaiot.domain.persistence.sql;

import lombok.Getter;

@Getter
public enum EntityType {
    TENANT(1),
    CUSTOMER(2),
    USER(3),
    DASHBOARD(4),
    ASSET(5),
    DEVICE(6),
    ALARM (7),
    CONVERTER(101),
    INTEGRATION(102),
    RULE_CHAIN (11),
    RULE_NODE (12),
    WIDGETS_BUNDLE (16),
    WIDGET_TYPE (17),
    ROLE(105),
    GROUP_PERMISSION(106),
    TENANT_PROFILE (20),
    DEVICE_PROFILE (21),
    ASSET_PROFILE (22),
    API_USAGE_STATE (23),
    TB_RESOURCE (24),
    OTA_PACKAGE (25),
    EDGE (26),
    RPC (27),
    QUEUE (28),
    NOTIFICATION_TARGET (29),
    NOTIFICATION_TEMPLATE (30),
    NOTIFICATION_REQUEST (31),
    NOTIFICATION (32),
    NOTIFICATION_RULE (33),
    ;

    private final int protoNumber; // Corresponds to EntityTypeProto

    EntityType(int protoNumber) {
        this.protoNumber = protoNumber;
    }
}
