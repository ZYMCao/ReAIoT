package cn.easttrans.reaiot.dao.sql;

import cn.easttrans.reaiot.domain.persistence.sql.Device;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 11:42
 **/
@Repository
public interface DeviceRepository extends ReactiveCrudRepository<Device, UUID> {
    Flux<Device> findByTenantId(UUID tenantId);
}
