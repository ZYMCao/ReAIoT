package cn.easttrans.reaiot.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.handler.codec.mqtt.MqttEncoder.INSTANCE;
import static io.netty.handler.logging.LogLevel.DEBUG;

@Slf4j
public class ReMqttClient implements MqttClient {

    /**
     * Post PUBLISH, pre PUBACK or PUBCOMP; client is the producer
     */
    private record MqttOutgoingPublishes(int messageId, ByteBuf payload, MqttPublishMessage message, MqttQoS qos) {
    }

    private final ConcurrentMap<Integer, MqttOutgoingPublishes> pendingOutgoingPublishes = new ConcurrentHashMap<>();

    /**
     * Post PUBREC, pre PUBREL; client is the consumer
     */
    private record MqttIncomingPublishes(MqttPublishMessage message) {
    }

    private final ConcurrentMap<Integer, MqttIncomingPublishes> pendingIncomingPublishes = new ConcurrentHashMap<>();

    /**
     * Post SUBSCRIBE, pre SUBACK
     */
    private record MqttPendingSubscription(String topic, MqttSubscribeMessage subscribeMessage) {
    }

    private final ConcurrentMap<Integer, MqttPendingSubscription> pendingSubscriptions = new ConcurrentHashMap<>();
    private final Set<String> topicsSubscribing = new HashSet<>();

    /**
     * Post SUBACK, pre UNSUBACK
     */
    private record MqttSubscription(String topic) {
    }

    private final ConcurrentMap<String, Set<MqttSubscription>> subscriptions = new ConcurrentHashMap<>();
    private final Set<String> topicsSubscribed = new HashSet<>();
    private final ConcurrentMap<MqttHandler, Set<MqttSubscription>> handlerToSubscription = new ConcurrentHashMap<>();

    /**
     * Post UNSUBSCRIBE, pre UNSUBACK
     */
    private record MqttPendingUnsubscription(String topic, MqttUnsubscribeMessage unsubscribeMessage) {
    }

    private final ConcurrentMap<Integer, MqttPendingUnsubscription> pendingUnsubscriptions = new ConcurrentHashMap<>();


    private final MqttClientConfig clientConfig;
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicInteger nextMessageId = new AtomicInteger(1);
    private Connection connection;

    public ReMqttClient(MqttClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public boolean isConnected() {
        return connected.get();
    }

    @Override
    public Connection connect() {
        return this.connect("localhost", 1883);
    }

    @Override
    public Connection connect(String host, int port) {
        log.info("Trying to connect to the Mqtt Broker deployed at {}:{} ...", host, port);

        return TcpClient.create()
                .host(host)
                .port(port)
                .doOnChannelInit((connectionObserver, channel, remoteAddress) -> {
                    channel.pipeline().addLast("mqttDecoder", new MqttDecoder(65536));
                    channel.pipeline().addLast("mqttEncoder", INSTANCE);
                    channel.pipeline().addLast("mqttHandler", new MqttChannelInboundHandler());
                })
                .doOnConnected(conexus ->
                        conexus.outbound()
                                .sendObject(clientConfig.toMqttConnectMessage())
                                .then()
                                .subscribe())
                .connect()
                .block();
//                .doOnNext(conexus ->
//                        conexus.inbound()
//                                .receive()
//                                .doOnNext(x -> log.info(String.valueOf(x)))
//                                .subscribe()
//                )
//                .flatMap(conexus ->
//                        conexus.inbound()
//                                .receive()
//                                .doOnNext(x -> log.info(String.valueOf(x)))
//                                .then()
//                )
    }

//    @Override
//    public Mono<Void> publish(String topic, ByteBuf payload, MqttQoS qos, boolean retain) {
//        return Mono.defer(() -> {
//            if (!isConnected()) {
//                return Mono.error(new IllegalStateException("Not connected"));
//            }
//
//            // ...
//        });
//    }

    @Override
    public void on(String topic, MqttHandler handler, MqttQoS qos) {
    }

    @Override
    public void off(String topic) {
    }

    @Override
    public Mono<Void> disconnect() {
        return Mono.fromRunnable(() -> {
            // ...
        });
    }

//    private int nextPacketId() {
//        // ...
//    }

    private static final class MqttChannelInboundHandler extends SimpleChannelInboundHandler<MqttMessage> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) {
            log.info("Processing msg: {}", msg);
            if (msg.decoderResult().isSuccess()) {
                switch (msg.fixedHeader().messageType()) {
                    /*
                     * Client 只发出 CONNECT 消息；不会接收到 CONNECT 消息: handleConnect(ctx, (MqttConnectMessage) msg);
                     * Client 只发出 SUBSCRIBE 消息；不会接收到 SUBSCRIBE 消息: handleSubscribe(ctx, (MqttSubscribeMessage) msg);
                     * Client 只发出 UNSUBSCRIBE 消息；不会接收到 UNSUBSCRIBE 消息: handleUnsubscribe(ctx, (MqttUnsubscribeMessage) msg);
                     * Client 只发出 DISCONNECT 消息；不会接收到 DISCONNECT 消息: handleDisconnect(ctx, msg);
                     */
                    case CONNECT -> {
                    }
                    case CONNACK -> handleConnAck(ctx, (MqttConnAckMessage) msg);
                    case PUBLISH ->
                            handlePublish(ctx, (MqttPublishMessage) msg); // QoS(0, 1, 2) 都会有 PUBLISH; producer only
                    case PUBACK -> handlePubAck(ctx, (MqttPubAckMessage) msg); // QoS(1) 才会有 PUBACK; consumer only
                    case PUBREC -> handlePubRec(ctx, msg); // QoS(2) 才会有 PUBREC; consumer only
                    case PUBREL -> handlePubRel(ctx, msg); // QoS(2) 才会有 PUBREL; producer only
                    case PUBCOMP -> handlePubComp(ctx, msg); // QoS(2) 才会有 PUBCOMP; consumer only
                    case SUBSCRIBE -> {
                    }
                    case SUBACK -> handleSubAck(ctx, (MqttSubAckMessage) msg);
                    case UNSUBSCRIBE -> {
                    }
                    case UNSUBACK -> handleUnsubAck(ctx, (MqttUnsubAckMessage) msg);
                    case PINGREQ -> handlePingReq(ctx, msg);
                    case PINGRESP -> handlePingResp(ctx, msg);
                    case DISCONNECT -> {
                    }
                    case AUTH -> {
                    }
                }
            } else {
                ctx.close();
            }
        }

        private void handleConnAck(ChannelHandlerContext ctx, MqttConnAckMessage connAckMsg) {
            switch (connAckMsg.variableHeader().connectReturnCode()) {
                case CONNECTION_ACCEPTED -> log.info(String.valueOf(connAckMsg));
                case CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD, CONNECTION_REFUSED_IDENTIFIER_REJECTED, CONNECTION_REFUSED_NOT_AUTHORIZED, CONNECTION_REFUSED_SERVER_UNAVAILABLE, CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION ->
                        log.error(String.valueOf(
                                new Exception("Mqtt连接失败: " + connAckMsg.variableHeader().connectReturnCode())
                        ));
            }
        }

        private void handlePublish(ChannelHandlerContext ctx, MqttPublishMessage publishMsg) {
        }

        private void handlePubAck(ChannelHandlerContext ctx, MqttPubAckMessage pubAckMsg) {
        }

        private void handlePubRec(ChannelHandlerContext ctx, MqttMessage pubRecMsg) {
        }

        private void handlePubRel(ChannelHandlerContext ctx, MqttMessage pubRelMsg) {
        }

        private void handlePubComp(ChannelHandlerContext ctx, MqttMessage pubCompMsg) {
        }

        private void handleSubAck(ChannelHandlerContext ctx, MqttSubAckMessage subAckMsg) {
        }

        private void handleUnsubAck(ChannelHandlerContext ctx, MqttUnsubAckMessage unsubAckMsg) {
        }

        private void handlePingReq(ChannelHandlerContext ctx, MqttMessage pingReqMsg) {
        }

        private void handlePingResp(ChannelHandlerContext ctx, MqttMessage pingRespMsg) {
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            log.info("Channel active!");
            ctx.fireChannelActive();
        }
    }
}