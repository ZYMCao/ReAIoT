package cn.easttrans.reaiot.service;

import com.hivemq.client.mqtt.mqtt3.Mqtt3RxClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import io.reactivex.disposables.Disposable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;

import static com.hivemq.client.mqtt.datatypes.MqttQos.AT_LEAST_ONCE;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/3/12 14:58
 **/
@RequiredArgsConstructor
@Service
@Slf4j
public class MqttDataReceiveService {
    private final Mqtt3RxClient client;
    private static final String USERNAME = "admin";
    private static final byte[] PASSWORD = "public".getBytes();
    private static final String SNJBZ = "/SNJBZ/#";

    @PostConstruct
    public Disposable init() {
        return client.connectWith()
                .simpleAuth()
                .username(USERNAME)
                .password(PASSWORD)
                .applySimpleAuth()
                .applyConnect()
                .doOnSuccess(connAck -> log.info("MqttDataReceiveService::init connects: {}", connAck))
                .doOnError(error -> log.error("MqttDataReceiveService::init failed to connect", error))
                .flatMapPublisher(connAck -> client.subscribePublishesWith()
                        .addSubscription()
                        .topicFilter(SNJBZ)
                        .qos(AT_LEAST_ONCE)
                        .applySubscription()
                        .applySubscribe()
                        .doOnSingle(subAck -> log.info("MqttDataReceiveService::init subscribed: {}", subAck))
                        .doOnNext(this::processMessage)
                        .doOnError(error -> log.error("MqttDataReceiveService::init failed with subscription error", error))
                )
                .subscribe();
    }

    private void processMessage(Mqtt3Publish publish) {
        publish.getPayload().ifPresentOrElse(
                byteBuffer -> log.info("{}: {}", publish.getTopic(), StandardCharsets.UTF_8.decode(byteBuffer.slice())),
                () -> log.error("MqttDataReceiveService::init received PUB that is null!!")
        );
    }
}
