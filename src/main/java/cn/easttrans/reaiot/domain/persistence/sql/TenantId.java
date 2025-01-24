package cn.easttrans.reaiot.domain.persistence.sql;

import java.util.UUID;

public record TenantId(UUID id) implements EntityId {
    public EntityType entityType() {
        return EntityType.TENANT;
    }
}
