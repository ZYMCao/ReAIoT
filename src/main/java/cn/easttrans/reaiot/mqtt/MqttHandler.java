package cn.easttrans.reaiot.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttQoS;

@FunctionalInterface
public interface MqttHandler {
    void handle(String topic, ByteBuf payload, MqttQoS qos);
}
