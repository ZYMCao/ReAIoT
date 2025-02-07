package cn.easttrans.reaiot.mqtt;

import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttVersion;

import java.util.Random;

import static io.netty.handler.codec.mqtt.MqttMessageType.CONNECT;
import static io.netty.handler.codec.mqtt.MqttQoS.AT_MOST_ONCE;
import static io.netty.handler.codec.mqtt.MqttVersion.MQTT_3_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public record MqttClientConfig(String username,
                               String password,
                               String clientId,
                               MqttLastWill lastWill,
                               MqttVersion protocolVersion,
                               boolean cleanSession,
                               int timeoutSeconds) {

    public MqttClientConfig(String username, String password) {
        this(
                username,
                password,
                randomId(),
                null,
                MQTT_3_1,
                true,
                60
        );
    }

    public MqttClientConfig() {
        this(
                null,
                null,
                randomId(),
                null,
                MQTT_3_1,
                true,
                60
        );
    }

    private static String randomId() {
        String[] options = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split("");
        return "ReAIoT/" + new Random().ints(8, 0, options.length)
                .mapToObj(i -> options[i])
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
    }

    public MqttConnectMessage toMqttConnectMessage() {
        return new MqttConnectMessage(
                new MqttFixedHeader(CONNECT, false, AT_MOST_ONCE, false, 0),
                new MqttConnectVariableHeader(
                        protocolVersion.protocolName(),
                        protocolVersion.protocolLevel(),
                        username != null,
                        password != null,
                        lastWill != null && lastWill.retain(),
                        lastWill != null ? lastWill.qos().value() : 0,
                        lastWill != null,
                        cleanSession,
                        timeoutSeconds
                ),
                new MqttConnectPayload(
                        clientId,
                        lastWill != null ? lastWill.topic() : null,
                        lastWill != null ? lastWill.message().getBytes(UTF_8) : null,
                        username,
                        password != null ? password.getBytes(UTF_8) : null
                )
        );
    }
}
