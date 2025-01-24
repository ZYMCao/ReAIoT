package cn.easttrans.reaiot.dao.sql;

import cn.easttrans.reaiot.domain.persistence.sql.DeviceProfile;
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
public interface DeviceProfileRepository extends ReactiveCrudRepository<DeviceProfile, UUID> {

    Flux<DeviceProfile> findByTenantId(UUID tenantId);

    Mono<DeviceProfile> findByNameAndTenantId(String name, UUID tenantId);

    @Query("SELECT * FROM device_profile WHERE tenant_id = :tenantId AND is_default = true")
    Flux<DeviceProfile> findDefaultDeviceProfilesByTenantId(UUID tenantId);
}
