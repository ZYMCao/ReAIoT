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
public enum CMPDataType {
    START(1, "开始打桩"),
    WORKING(2, "打桩进行中"),
    END(3, "结束打桩"),
    REISSUE(4, "补传数据"),
    SEGMENT(5, "补传段数据？"),
    ;

    final int RAW;
    final String description;

    private static CMPDataType fromRaw(int raw) {
        return switch (raw) {
            case 1 -> START;
            case 2 -> WORKING;
            case 3 -> END;
            case 4 -> REISSUE;
            case 5 -> SEGMENT;
            default -> throw new IllegalArgumentException("CMPDataType only accepts integers in range [1, 5]: " + raw);
        };
    }

    private static CMPDataType fromRaw(String raw) {
        return switch (raw) {
            case "1" -> START;
            case "2" -> WORKING;
            case "3" -> END;
            case "4" -> REISSUE;
            case "5" -> SEGMENT;
            default -> throw new IllegalArgumentException("CMPDataType only accepts integers in range [1, 5]: " + raw);
        };
    }

    @Override
    public String toString() {
        return String.valueOf(this.RAW);
    }

    public static class Deserializer extends JsonDeserializer<CMPDataType> {
        @Override
        public CMPDataType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return parser.currentToken().isNumeric() ? CMPDataType.fromRaw(parser.getIntValue()) : CMPDataType.fromRaw(parser.getText());
        }
    }
}
