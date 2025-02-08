package cn.easttrans.reaiot.mqtt;

import io.netty.handler.codec.mqtt.MqttQoS;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

/**
 * 一个 Mqtt 客户端的基本功能：连接，断开连接，发布，订阅，解除订阅
 */
public interface MqttClient {
    Connection connect();
    Connection connect(String host, int port);
    Mono<Void> disconnect();
//    Mono<Void> publish(String topic, ByteBuf payload, MqttQoS qos, boolean retain);
    void on(String topic, MqttHandler handler, MqttQoS qos);
    void off(String topic);
    boolean isConnected();
}
