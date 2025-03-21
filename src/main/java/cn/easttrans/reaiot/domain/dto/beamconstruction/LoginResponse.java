package cn.easttrans.reaiot.domain.dto.beamconstruction;

import java.io.Serializable;

public record LoginResponse(String username, String password, String token) implements Serializable {
}
