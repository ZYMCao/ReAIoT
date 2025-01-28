package cn.easttrans.reaiot.controller;

import cn.easttrans.reaiot.domain.persistence.sql.DeviceProfileEntity;
import cn.easttrans.reaiot.service.DeviceProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/22 17:16
 * @Version: 1.0
 **/
@RequestMapping("/api/device-profiles")
@RequiredArgsConstructor
@RestController
@Slf4j
public final class DeviceProfileController {
    private final DeviceProfileService deviceProfileService;
    @GetMapping("/default/{tenantId}")
    public Flux<DeviceProfileEntity> getDefaultDeviceProfilesByTenantId(@PathVariable UUID tenantId) {
        return deviceProfileService.findDefaultDeviceProfilesByTenantId(tenantId);
    }
}
