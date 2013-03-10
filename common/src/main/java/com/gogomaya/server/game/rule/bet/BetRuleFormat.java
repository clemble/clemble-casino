package com.gogomaya.server.game.rule.bet;

import java.io.IOException;
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

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.hibernate.AbstractImmutableUserType;

public class BetRuleFormat {

    final private static String BET_TYPE_TOKEN = "betType";
    final private static String PRICE_TOKEN = "price";
    final private static String MIN_BET_TOKEN = "minBet";
    final private static String MAX_BET_TOKEN = "maxBet";

    public static class CustomBetRuleDeserializer extends JsonDeserializer<BetRule> {

        @Override
        public BetRule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            BetType betType = null;
            long price = 0;
            long maxPrice = 0;
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String currentName = jp.getCurrentName();
                if (BET_TYPE_TOKEN.equals(currentName)) {
                    betType = BetType.valueOf(jp.nextTextValue());
                } else if (PRICE_TOKEN.equals(currentName) || MIN_BET_TOKEN.equals(currentName)) {
                    price = jp.nextLongValue(0);
                } else if (MAX_BET_TOKEN.equals(currentName)) {
                    maxPrice = jp.nextLongValue(0);
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
                return UnlimitedBetRule.INCTANCE;
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

    public static class CustomBetRuleType<T extends BetRule> extends AbstractImmutableUserType<T> {
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
                return FixedBetRule.create(rs.getLong(names[1]));
            case Limited:
                return LimitedBetRule.create(rs.getLong(names[1]), rs.getLong(names[2]));
            case Unlimited:
                return UnlimitedBetRule.INCTANCE;
            }
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            if (value instanceof UnlimitedBetRule) {
                st.setString(index++, BetType.Unlimited.name());
                st.setLong(index++, 0);
                st.setLong(index++, 0);
            } else if (value instanceof FixedBetRule) {
                st.setString(index++, BetType.Fixed.name());
                st.setLong(index++, ((FixedBetRule) value).getPrice());
                st.setLong(index++, 0);
            } else if (value instanceof LimitedBetRule) {
                st.setString(index++, BetType.Limited.name());
                st.setLong(index++, ((LimitedBetRule) value).getMinBet());
                st.setLong(index++, ((LimitedBetRule) value).getMaxBet());
            } else {
                throw GogomayaException.create(GogomayaError.ServerCriticalError);
            }
        }

    }

}
