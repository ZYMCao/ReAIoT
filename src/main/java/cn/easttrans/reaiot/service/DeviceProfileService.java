package cn.easttrans.reaiot.service;

import cn.easttrans.reaiot.dao.sql.DeviceProfileRepository;
import cn.easttrans.reaiot.domain.persistence.sql.DeviceProfile;
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

    public Mono<DeviceProfile> createDeviceProfile(DeviceProfile deviceProfile) {
        return deviceProfileRepository.save(deviceProfile);
    }

    public Flux<DeviceProfile> getDeviceProfilesByTenantId(UUID tenantId) {
        return deviceProfileRepository.findByTenantId(tenantId);
    }

    public Mono<DeviceProfile> getDeviceProfileByNameAndTenantId(String name, UUID tenantId) {
        return deviceProfileRepository.findByNameAndTenantId(name, tenantId);
    }
}
