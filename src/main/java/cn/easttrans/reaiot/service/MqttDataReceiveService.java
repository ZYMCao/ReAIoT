package cn.easttrans.reaiot.service;

import cn.easttrans.reaiot.dao.nosql.CMPRawReactiveRepository;
import cn.easttrans.reaiot.domain.dto.CMPRaw;
import cn.easttrans.reaiot.domain.dto.WrappedCMPRaw;
import com.hivemq.client.mqtt.datatypes.MqttTopic;
import com.hivemq.client.mqtt.mqtt3.Mqtt3RxClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.Optional;

import static cn.easttrans.reaiot.utils.MqttMsgToRecordsUtils.decodeByteBuffer;
import static com.hivemq.client.mqtt.datatypes.MqttQos.AT_LEAST_ONCE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static reactor.adapter.rxjava.RxJava2Adapter.monoToSingle;

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
    private final CMPRawReactiveRepository cmpRawReactiveRepository;
    private static final String USERNAME = "admin";
    private static final byte[] PASSWORD = "public".getBytes();
    private static final String WORKING = "/SNJBZ/+/thing/event/working/#";

    @PostConstruct
    public Disposable init() {
        return client.connectWith()
                .simpleAuth().username(USERNAME).password(PASSWORD).applySimpleAuth()
                .applyConnect()
                .doOnSuccess(connAck -> log.info("MqttDataReceiveService::init connects: {}", connAck))
                .doOnError(e -> log.error("MqttDataReceiveService::init failed to connect", e))
                .flatMapPublisher(connAck ->
                        client.subscribePublishesWith()
                                .addSubscription()
                                .topicFilter(WORKING)
                                .qos(AT_LEAST_ONCE)
                                .applySubscription()
                                .applySubscribe()
                                .doOnSingle(subAck -> log.info("MqttDataReceiveService::init subscribed: {}", subAck))
                                .map(this::processMessage)
                                .doOnError(e -> log.error("MqttDataReceiveService::init failed with subscription error", e))
                )
                .subscribe();
    }

    /**
     * this::persistPayload
     */
    private Single<CMPRaw> processMessage(Mqtt3Publish publish) {
        return Single.fromCallable(publish::getPayload).flatMap(payload -> {
            if (payload.isPresent()) {
                return persistPayload(publish.getPayload(), publish.getTopic());
            } else {
                return Single.never(); // Handle empty payload
            }
        });
    }

    /**
     * cmpRawReactiveRepository::save
     */
    private Single<CMPRaw> persistPayload(Optional<ByteBuffer> payload, MqttTopic topic) {
        ByteBuffer byteBuffer = payload.get();
        Mono<CMPRaw> saved = cmpRawReactiveRepository.save(decodeByteBuffer(byteBuffer, WrappedCMPRaw.class).message())
                .onErrorResume(e -> {
                    log.error("Persistence failed! {}: {}", topic, UTF_8.decode(byteBuffer.slice()), e);
                    return Mono.never(); // Suppress error after logging
                });
        return monoToSingle(saved);
    }
}
