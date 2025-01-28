package cn.easttrans.reaiot.service;

import cn.easttrans.reaiot.dao.sql.DeviceProfileRepository;
import cn.easttrans.reaiot.domain.persistence.sql.DeviceProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 11:48
 **/
@RequiredArgsConstructor
@Service
public class DeviceProfileService {
    private final DeviceProfileRepository deviceProfileRepository;

    public Mono<DeviceProfileEntity> createDeviceProfile(DeviceProfileEntity deviceProfile) {
        return deviceProfileRepository.save(deviceProfile);
    }

    public Flux<DeviceProfileEntity> getDeviceProfilesByTenantId(UUID tenantId) {
        return deviceProfileRepository.findByTenantId(tenantId);
    }

    public Mono<DeviceProfileEntity> getDeviceProfileByNameAndTenantId(String name, UUID tenantId) {
        return deviceProfileRepository.findByNameAndTenantId(name, tenantId);
    }

    public Flux<DeviceProfileEntity> findDefaultDeviceProfilesByTenantId(UUID tenantId) {
        return deviceProfileRepository.findDefaultDeviceProfilesByTenantId(tenantId);
    }
}
