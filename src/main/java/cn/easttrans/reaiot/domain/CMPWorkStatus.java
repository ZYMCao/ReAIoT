package cn.easttrans.reaiot.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Getter
@RequiredArgsConstructor
public enum CMPWorkStatus {
    ONLINE(0, "在线"),
    DOWN(1, "下钻"),
    UP(2, "提钻"),
    ;

    final int RAW;
    final String description;

    @Override
    public String toString() {
        return String.valueOf(this.RAW);
    }

    public static CMPWorkStatus fromRaw(int raw) {
        return switch (raw) {
            case 0 -> ONLINE;
            case 1 -> DOWN;
            case 2 -> UP;
            default ->
                    throw new IllegalArgumentException("CMPWorkStatus only accepts integers in range [0, 2]: " + raw);
        };
    }

    public static CMPWorkStatus fromRaw(String raw) {
        return switch (raw) {
            case "0" -> ONLINE;
            case "1" -> DOWN;
            case "2" -> UP;
            default -> throw new IllegalArgumentException("CMPWorkStatus only accepts String in range [0, 2]: " + raw);
        };
    }

    public static class Deserializer extends JsonDeserializer<CMPWorkStatus> {
        @Override
        public CMPWorkStatus deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return parser.currentToken().isNumeric() ?
                    CMPWorkStatus.fromRaw(parser.getIntValue()) :
                    CMPWorkStatus.fromRaw(parser.getText());
        }
    }
}
