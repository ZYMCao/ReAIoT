package cn.easttrans.reaiot.mqtt;

import io.netty.handler.codec.mqtt.MqttQoS;

public record MqttLastWill(String topic,
                           String message,
                           boolean retain,
                           MqttQoS qos) {
}
