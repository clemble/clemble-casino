package com.gogomaya.server.game.rule.giveup;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;

public class GiveUpRuleFormat {

    final public static SerializableString LOOSE_TYPE_TOKEN = new SerializedString("looseType");
    final public static SerializableString MIN_PART_TOKEN = new SerializedString("minPart");

    public static class CustomGiveUpRuleDeserializer extends JsonDeserializer<GiveUpRule> {

        @Override
        public GiveUpRule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.nextFieldName(LOOSE_TYPE_TOKEN))
                throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
            LoosingType loosingType = LoosingType.valueOf(jp.nextTextValue());
            switch (loosingType) {
            case All:
                return LooseAllGiveUpRule.INSTANCE;
            case Lost:
                return LooseLostGiveUpRule.INSTANCE;
            case MinPart:
                if (!jp.nextFieldName(MIN_PART_TOKEN))
                    throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
                return LooseMinGiveUpRule.create(jp.nextIntValue(0));
            }
            throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
        }

    }

    public static class CustomGiveUpRuleSerializer extends JsonSerializer<GiveUpRule> {

        @Override
        public void serialize(GiveUpRule value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeFieldName(LOOSE_TYPE_TOKEN);
            jgen.writeString(value.getLoosingType().name());
            if (value instanceof LooseMinGiveUpRule) {
                jgen.writeFieldName(MIN_PART_TOKEN);
                jgen.writeNumber(((LooseMinGiveUpRule) value).getMinPart());
            } else if (!(value instanceof LooseAllGiveUpRule) && !(value instanceof LooseLostGiveUpRule)) {
                throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
            }
            jgen.writeEndObject();
        }
    }
}
