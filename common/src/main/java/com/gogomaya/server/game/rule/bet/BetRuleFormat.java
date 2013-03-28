package com.gogomaya.server.game.rule.bet;

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
import com.gogomaya.server.hibernate.ImmutableHibernateType;

public class BetRuleFormat {
    
    final public static BetRule DEFAULT_BET_RULE = UnlimitedBetRule.INSTANCE;

    final public static String BET_TYPE_TOKEN = "betType";
    final public static String PRICE_TOKEN = "price";
    final public static String MIN_BET_TOKEN = "minBet";
    final public static String MAX_BET_TOKEN = "maxBet";

    public static class CustomBetRuleDeserializer extends JsonDeserializer<BetRule> {

        @Override
        public BetRule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            BetType betType = null;
            int price = 0;
            int maxPrice = 0;

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String currentName = jp.getCurrentName();
                if (BET_TYPE_TOKEN.equals(currentName)) {
                    betType = BetType.valueOf(jp.nextTextValue());
                } else if (PRICE_TOKEN.equals(currentName) || MIN_BET_TOKEN.equals(currentName)) {
                    price = jp.nextIntValue(0);
                } else if (MAX_BET_TOKEN.equals(currentName)) {
                    maxPrice = jp.nextIntValue(0);
                } else {
                    throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
                }
            }

            switch (betType) {
            case Fixed:
                return FixedBetRule.create(price);
            case Limited:
                return LimitedBetRule.create(price, maxPrice);
            case Unlimited:
                return UnlimitedBetRule.INSTANCE;
            }
            return null;
        }

    }

    public static class CustomBetRuleSerializer extends JsonSerializer<BetRule> {

        @Override
        public void serialize(BetRule value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeFieldName(BET_TYPE_TOKEN);
            jgen.writeString(value.getRuleType().name());
            if (value instanceof FixedBetRule) {
                jgen.writeFieldName(PRICE_TOKEN);
                jgen.writeNumber(((FixedBetRule) value).getPrice());
            } else if (value instanceof LimitedBetRule) {
                jgen.writeFieldName(MIN_BET_TOKEN);
                jgen.writeNumber(((LimitedBetRule) value).getMinBet());
                jgen.writeFieldName(MAX_BET_TOKEN);
                jgen.writeNumber(((LimitedBetRule) value).getMaxBet());
            } else if (!(value instanceof UnlimitedBetRule)) {
                throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
            }
            jgen.writeEndObject();
        }

    }

    public static class BetRuleHibernateType<T extends BetRule> extends ImmutableHibernateType<T> {
        final private int[] TYPES = { Types.VARCHAR, Types.INTEGER, Types.INTEGER };

        @Override
        public int[] sqlTypes() {
            return TYPES;
        }

        @Override
        public Class<?> returnedClass() {
            return BetRule.class;
        }

        @Override
        public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
            BetType betType = BetType.valueOf(rs.getString(names[0]));
            switch (betType) {
            case Fixed:
                return FixedBetRule.create(rs.getInt(names[1]));
            case Limited:
                return LimitedBetRule.create(rs.getInt(names[1]), rs.getInt(names[2]));
            case Unlimited:
                return UnlimitedBetRule.INSTANCE;
            }
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            BetRule betRule = value != null ? (BetRule) value : DEFAULT_BET_RULE;
            st.setString(index++, betRule.getRuleType().name());
            switch(betRule.getRuleType()) {
            case Fixed:
                st.setLong(index++, ((FixedBetRule) value).getPrice());
                st.setLong(index++, ((FixedBetRule) value).getPrice());
                break;
            case Limited:
                st.setLong(index++, ((LimitedBetRule) value).getMinBet());
                st.setLong(index++, ((LimitedBetRule) value).getMaxBet());
                break;
            case Unlimited:
                st.setLong(index++, 0);
                st.setLong(index++, 0);
                break;
            default:
                throw GogomayaException.create(GogomayaError.ServerCriticalError);
            }
        }

    }

    public static class CustomBetRuleByteBufferStream implements ByteBufferStream<BetRule> {

        @Override
        public ByteBuffer write(BetRule betRule, ByteBuffer buffer) {

            buffer.put((byte) betRule.getRuleType().ordinal());

            switch (betRule.getRuleType()) {
            case Fixed:
                buffer.putInt(((FixedBetRule) betRule).getPrice());
                break;
            case Limited:
                buffer.putInt(((LimitedBetRule) betRule).getMinBet())
                    .putInt(((LimitedBetRule) betRule).getMaxBet());
                break;
            case Unlimited:
                break;
            }
            return buffer;
        }

        @Override
        public BetRule read(ByteBuffer buffer) {
            int betType = buffer.get();

            if (betType == BetType.Fixed.ordinal()) {
                return FixedBetRule.create(buffer.getInt());
            } else if (betType == BetType.Limited.ordinal()) {
                return LimitedBetRule.create(buffer.getInt(), buffer.getInt());
            } else if (betType == BetType.Unlimited.ordinal()) {
                return UnlimitedBetRule.INSTANCE;
            }

            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }
    }

}
