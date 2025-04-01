package cn.easttrans.reaiot.agentic.domain.dto.chat;

import java.io.Serializable;

public record Question(
        String user,
        String system,
        String context) implements Serializable {
}
