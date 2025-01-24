package cn.easttrans.reaiot.controller;

import cn.easttrans.reaiot.service.DeviceProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/22 17:16
 * @Version: 1.0
 **/
@RequiredArgsConstructor
@RestController
@Slf4j
public final class WebfluxController {
    private final DeviceProfileService deviceProfileService;


}
