package cn.easttrans.reaiot.dao.sql;

import cn.easttrans.reaiot.domain.persistence.sql.DeviceProfileEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 11:38
 **/
@Repository
public interface DeviceProfileRepository extends ReactiveCrudRepository<DeviceProfileEntity, UUID> {

    Flux<DeviceProfileEntity> findByTenantId(UUID tenantId);

    Mono<DeviceProfileEntity> findByNameAndTenantId(String name, UUID tenantId);

    @Query("SELECT * FROM device_profile WHERE tenant_id = :tenantId AND is_default = true")
    Flux<DeviceProfileEntity> findDefaultDeviceProfilesByTenantId(UUID tenantId);
}
