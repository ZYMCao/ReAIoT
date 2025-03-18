package cn.easttrans.reaiot.utils;

import cn.easttrans.reaiot.domain.dto.CMPRaw;
import cn.easttrans.reaiot.domain.dto.WrappedCMPRaw;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/3/13 11:20
 **/
public class MqttMsgToRecordsUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <W> W decodeByteBuffer(ByteBuffer byteBuffer, Class<W> wrappedClass) {
        try {
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            return MAPPER.readValue(bytes, wrappedClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}