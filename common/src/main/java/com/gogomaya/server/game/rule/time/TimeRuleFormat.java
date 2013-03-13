package com.gogomaya.server.game.rule.time;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.gogomaya.server.buffer.ByteBufferStream;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.hibernate.AbstractImmutableUserType;

public class TimeRuleFormat {

    final public static String TIME_TYPE_TOKEN = "timeType";
    final public static String TIME_BREACH_TOKEN = "timeBreach";
    final public static String TIME_LIMIT_TOKEN = "timeLimit";

    public static class CustomTimeRuleDeserializer extends JsonDeserializer<TimeRule> {

        @Override
        public TimeRule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            TimeRuleType timeRuleType = TimeRuleType.Unlimited;
            TimeBreachBehavior timeBreachBehavior = TimeBreachBehavior.PlayerLoose;
            int timeLimit = Integer.MAX_VALUE;

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String currentName = jp.getCurrentName();
                if (TIME_TYPE_TOKEN.equals(currentName)) {
                    timeRuleType = TimeRuleType.valueOf(jp.nextTextValue());
                } else if (TIME_BREACH_TOKEN.equals(currentName)) {
                    timeBreachBehavior = TimeBreachBehavior.valueOf(jp.nextTextValue());
                } else if (TIME_LIMIT_TOKEN.equals(currentName)) {
                    timeLimit = jp.nextIntValue(0);
                } else {
                    throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
                }
            }

            switch (timeRuleType) {
            case Unlimited:
                return UnlimitedTimeRule.INSTANCE;
            case LimitedGameTime:
                return LimitedGameTimeRule.create(timeBreachBehavior, timeLimit);
            case LimitedMoveTime:
                return LimitedMoveTimeRule.create(timeBreachBehavior, timeLimit);
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
                return UnlimitedTimeRule.INSTANCE;
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

    public static class CustomTimeRuleByteBufferStream implements ByteBufferStream<TimeRule> {

        @Override
        public ByteBuffer write(TimeRule timeRule, ByteBuffer writeBuffer) {
            writeBuffer.put((byte) timeRule.getRuleType().ordinal()).put((byte) timeRule.getBreachBehavior().ordinal());

            switch (timeRule.getRuleType()) {
            case LimitedGameTime:
                writeBuffer.putInt(((LimitedGameTimeRule) timeRule).getGameTimeLimit());
                break;
            case LimitedMoveTime:
                writeBuffer.putInt(((LimitedMoveTimeRule) timeRule).getMoveTimeLimit());
                break;
            case Unlimited:
                break;
            default:
                throw GogomayaException.create(GogomayaError.ServerCriticalError);
            }

            return writeBuffer;
        }

        @Override
        public TimeRule read(ByteBuffer readBuffer) {
            byte timeRule = readBuffer.get();
            TimeRuleType timeRuleType = timeRule == TimeRuleType.LimitedGameTime.ordinal() ? TimeRuleType.LimitedGameTime
                    : timeRule == TimeRuleType.LimitedMoveTime.ordinal() ? TimeRuleType.LimitedMoveTime
                            : timeRule == TimeRuleType.Unlimited.ordinal() ? TimeRuleType.Unlimited : null;

            byte breachType = readBuffer.get();
            TimeBreachBehavior breachBehavior = breachType == TimeBreachBehavior.DoNothing.ordinal() ? TimeBreachBehavior.DoNothing
                    : breachType == TimeBreachBehavior.PlayerLoose.ordinal() ? TimeBreachBehavior.PlayerLoose : null;

            switch (timeRuleType) {
            case LimitedGameTime:
                return LimitedGameTimeRule.create(breachBehavior, readBuffer.getInt());
            case LimitedMoveTime:
                return LimitedMoveTimeRule.create(breachBehavior, readBuffer.getInt());
            case Unlimited:
                return UnlimitedTimeRule.INSTANCE;
            default:
                throw GogomayaException.create(GogomayaError.ServerCriticalError);
            }
        }

    }
}
