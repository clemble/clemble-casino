package com.gogomaya.server.game.rule.time;

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

public class TimeRuleFormat {

    final public static SerializableString TIME_TYPE_TOKEN = new SerializedString("timeType");
    final public static SerializableString TIME_BREACH_TOKEN = new SerializedString("timeBreach");
    final public static SerializableString TIME_LIMIT_TOKEN = new SerializedString("timeLimit");

    public static class CustomTimeRuleDeseriler extends JsonDeserializer<TimeRule> {

        @Override
        public TimeRule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.nextFieldName(TIME_TYPE_TOKEN))
                throw GogomayaException.create(GogomayaError.ClientJsonFormatError);

            TimeRuleType timeRuleType = TimeRuleType.valueOf(jp.nextTextValue());
            TimeBreachBehavior timeBreachBehavior = jp.nextFieldName(TIME_BREACH_TOKEN) ? TimeBreachBehavior.valueOf(jp.nextTextValue())
                    : TimeBreachBehavior.PlayerLoose;

            switch (timeRuleType) {
            case Unlimited:
                return UnlimitedTimeRule.create(timeBreachBehavior);
            case LimitedGameTime:
                if (!jp.nextFieldName(TIME_LIMIT_TOKEN))
                    throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
                return LimitedGameTimeRule.create(timeBreachBehavior, jp.nextIntValue(0));
            case LimitedMoveTime:
                if (!jp.nextFieldName(TIME_LIMIT_TOKEN))
                    throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
                return LimitedMoveTimeRule.create(timeBreachBehavior, jp.nextIntValue(0));
            }
            throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
        }

    }

    public static class CustomTimeRuleSerializer extends JsonSerializer<TimeRule> {

        @Override
        public void serialize(TimeRule value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeFieldName(TIME_TYPE_TOKEN);
            jgen.writeString(value.getRuleType().name());
            jgen.writeFieldName(TIME_BREACH_TOKEN);
            jgen.writeString(value.getBreachBehavior().name());
            if (value instanceof LimitedMoveTimeRule) {
                jgen.writeFieldName(TIME_LIMIT_TOKEN);
                jgen.writeNumber(((LimitedMoveTimeRule) value).getMoveTimeLimit());
            } else if (value instanceof LimitedGameTimeRule) {
                jgen.writeFieldName(TIME_LIMIT_TOKEN);
                jgen.writeNumber(((LimitedGameTimeRule) value).getGameTimeLimit());
            } else if (!(value instanceof UnlimitedTimeRule)) {
                throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
            }
            jgen.writeEndObject();
        }

    }
}
