package cn.easttrans.reaiot.agentic.domain.dto.beamconstruction;

import java.io.Serializable;

/**
 * 用户登录对象
 * @param username
 * @param password
 * @param suffix   URL后缀，判断用户属于哪个项目
 */
public record LoginRequest(
        String username,
        String password,
//        String otherToken,
        String suffix
) implements Serializable {
}
