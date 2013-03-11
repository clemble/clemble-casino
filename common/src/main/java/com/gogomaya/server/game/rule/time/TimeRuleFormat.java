package com.gogomaya.server.game.rule.time;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.hibernate.AbstractImmutableUserType;

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

    public static class CustomTimeRuleType extends AbstractImmutableUserType<TimeRule> {

        final private int[] TYPES = { Types.VARCHAR, Types.INTEGER, Types.INTEGER };

        @Override
        public int[] sqlTypes() {
            return TYPES;
        }

        @Override
        public Class<?> returnedClass() {
            return TimeRule.class;
        }

        @Override
        public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
            TimeRuleType ruleType = TimeRuleType.valueOf(rs.getString(names[0]));
            TimeBreachBehavior breachBehavior = TimeBreachBehavior.valueOf(rs.getString(names[1]));

            switch (ruleType) {
            case LimitedGameTime:
                return LimitedGameTimeRule.create(breachBehavior, rs.getInt(names[2]));
            case LimitedMoveTime:
                return LimitedMoveTimeRule.create(breachBehavior, rs.getInt(names[2]));
            case Unlimited:
                return UnlimitedTimeRule.create(breachBehavior);
            }
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            TimeRuleType timeRule = ((TimeRule) value).getRuleType();
            st.setString(index++, timeRule.name());
            st.setString(index++, ((TimeRule) value).getBreachBehavior().name());
            switch (timeRule) {
            case LimitedGameTime:
                st.setInt(index++, ((LimitedGameTimeRule) value).getGameTimeLimit());
                break;
            case LimitedMoveTime:
                st.setInt(index++, ((LimitedMoveTimeRule) value).getMoveTimeLimit());
                break;
            case Unlimited:
                st.setInt(index++, 0);
                break;

            }
        }

    }
}
