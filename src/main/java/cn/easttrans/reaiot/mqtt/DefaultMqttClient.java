package cn.easttrans.reaiot.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.codec.mqtt.MqttUnsubAckMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static io.netty.handler.codec.mqtt.MqttEncoder.INSTANCE;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@RequiredArgsConstructor
public class DefaultMqttClient {
    private final MqttClientConfig clientConfig;
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicInteger nextMessageId = new AtomicInteger(1);
    private Connection connection;

    public Connection connect() {
        return connect("localhost", 1883);
    }

    public Connection connect(String host, int port) {

        log.info("Mqtt Broker deployed at {}:{} 尝试连接中 ...", host, port);

        return TcpClient
                .create()
                .option(CONNECT_TIMEOUT_MILLIS, 120000)
                .host(host)
                .port(port)
                .doOnChannelInit((connectionObserver, channel, remoteAddress) -> {
                            channel.pipeline().addLast("logRawBytes", new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    if (msg instanceof ByteBuf byteBuf) {
                                        log.info("Raw bytes: {}", byteBuf.toString(UTF_8));
                                    }
                                    super.channelRead(ctx, msg);
                                }
                            });
                            channel.pipeline().addLast("mqttDecoder", new MqttDecoder(65536));
                            channel.pipeline().addLast("mqttEncoder", INSTANCE);
                            channel.pipeline().addLast("mqttHandler", new DefaultMqttClient.MqttChannelInboundHandler());
                        }
                )
                .doOnConnected(conexus -> {
                    var msgInit = clientConfig.toMqttConnectMessage();
                    log.info("about to send mqtt CONNECT message: {}", msgInit);
                    conexus.outbound()
                            .sendObject(msgInit)
                            .then()
                            .subscribe();
                })
                .doOnDisconnected(conexus -> log.info("The Tcp connection was stopped."))
                .connectNow();
    }

    public Mono<Void> disconnect() {
        return null;
    }

    public Void on(String topic, MqttHandler handler, MqttQoS qos) {
        return null;
    }

    public void off(String topic) {

    }

    public boolean isConnected() {
        return connected.get();
    }


    @RequiredArgsConstructor
    private static final class MqttChannelInboundHandler extends SimpleChannelInboundHandler<MqttMessage> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) {
            log.info("Processing msg: {}", msg);
//            if (msg.decoderResult().isSuccess()) {
//                switch (msg.fixedHeader().messageType()) {
//                    /*
//                     * Client 只发出 CONNECT 消息；不会接收到 CONNECT 消息: handleConnect(ctx, (MqttConnectMessage) msg);
//                     * Client 只发出 SUBSCRIBE 消息；不会接收到 SUBSCRIBE 消息: handleSubscribe(ctx, (MqttSubscribeMessage) msg);
//                     * Client 只发出 UNSUBSCRIBE 消息；不会接收到 UNSUBSCRIBE 消息: handleUnsubscribe(ctx, (MqttUnsubscribeMessage) msg);
//                     * Client 只发出 DISCONNECT 消息；不会接收到 DISCONNECT 消息: handleDisconnect(ctx, msg);
//                     */
//                    case CONNECT -> {
//                    }
//                    case CONNACK -> handleConnAck(ctx, (MqttConnAckMessage) msg);
//                    case PUBLISH ->
//                            handlePublish(ctx, (MqttPublishMessage) msg); // QoS(0, 1, 2) 都会有 PUBLISH; producer only
//                    case PUBACK -> handlePubAck(ctx, (MqttPubAckMessage) msg); // QoS(1) 才会有 PUBACK; consumer only
//                    case PUBREC -> handlePubRec(ctx, msg); // QoS(2) 才会有 PUBREC; consumer only
//                    case PUBREL -> handlePubRel(ctx, msg); // QoS(2) 才会有 PUBREL; producer only
//                    case PUBCOMP -> handlePubComp(ctx, msg); // QoS(2) 才会有 PUBCOMP; consumer only
//                    case SUBSCRIBE -> {
//                    }
//                    case SUBACK -> handleSubAck(ctx, (MqttSubAckMessage) msg);
//                    case UNSUBSCRIBE -> {
//                    }
//                    case UNSUBACK -> handleUnsubAck(ctx, (MqttUnsubAckMessage) msg);
//                    case PINGREQ -> handlePingReq(ctx, msg);
//                    case PINGRESP -> handlePingResp(ctx, msg);
//                    case DISCONNECT -> {
//                    }
//                    case AUTH -> {
//                    }
//                }
//            }
//            else {
//                ctx.close();
//            }
        }

        private void handleConnAck(ChannelHandlerContext ctx, MqttConnAckMessage connAckMsg) {
            switch (connAckMsg.variableHeader().connectReturnCode()) {
                case CONNECTION_ACCEPTED -> log.info(String.valueOf(connAckMsg));
                case CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD, CONNECTION_REFUSED_IDENTIFIER_REJECTED, CONNECTION_REFUSED_NOT_AUTHORIZED, CONNECTION_REFUSED_SERVER_UNAVAILABLE, CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION ->
                        log.error(String.valueOf(connAckMsg));
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
    }
}
