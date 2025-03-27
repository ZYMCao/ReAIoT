package cn.easttrans.reaiot.agentic.domain.chat;

import java.io.Serializable;

public record Question(
        String user,
        String system,
        String context) implements Serializable {
}
