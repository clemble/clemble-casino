package com.gogomaya.server.game.rule.giveup;

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

public class GiveUpRuleFormat {
    
    final public static GiveUpRule DEFAULT_GIVE_UP_RULE = LooseAllGiveUpRule.INSTANCE;

    final public static String LOOSE_TYPE_TOKEN = "looseType";
    final public static String MIN_PART_TOKEN = "minPart";

    public static class CustomGiveUpRuleDeserializer extends JsonDeserializer<GiveUpRule> {

        @Override
        public GiveUpRule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            LoosingType loosingType = null;
            int minPart = 100;

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String currentName = jp.getCurrentName();
                if (LOOSE_TYPE_TOKEN.equals(currentName)) {
                    loosingType = LoosingType.valueOf(jp.nextTextValue());
                } else if (MIN_PART_TOKEN.equals(currentName)) {
                    minPart = jp.nextIntValue(0);
                } else {
                    throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
                }
            }

            switch (loosingType) {
            case All:
                return LooseAllGiveUpRule.INSTANCE;
            case Lost:
                return LooseLostGiveUpRule.INSTANCE;
            case MinPart:
                return LooseMinGiveUpRule.create(minPart);
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

    public static class CustomGiveUpRuleType extends AbstractImmutableUserType<GiveUpRule> {

        final private int[] TYPES = { Types.VARCHAR, Types.INTEGER };

        @Override
        public int[] sqlTypes() {
            return TYPES;
        }

        @Override
        public Class<?> returnedClass() {
            return GiveUpRule.class;
        }

        @Override
        public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
            LoosingType loosingType = LoosingType.valueOf(rs.getString(names[0]));
            switch (loosingType) {
            case All:
                return LooseAllGiveUpRule.INSTANCE;
            case Lost:
                return LooseLostGiveUpRule.INSTANCE;
            case MinPart:
                return LooseMinGiveUpRule.create(rs.getInt(names[1]));
            }
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            GiveUpRule giveUpRule = value != null ? (GiveUpRule) value : DEFAULT_GIVE_UP_RULE;
            st.setString(index++, giveUpRule.getLoosingType().name());
            st.setInt(index, value instanceof LooseMinGiveUpRule ? ((LooseMinGiveUpRule) value).getMinPart() : 0);
        }

    }

    public static class CustomGiveUpRuleByteBufferStream implements ByteBufferStream<GiveUpRule> {

        @Override
        public ByteBuffer write(GiveUpRule value, ByteBuffer writeBuffer) {
            writeBuffer.put((byte) value.getLoosingType().ordinal());

            switch (value.getLoosingType()) {
            case MinPart:
                writeBuffer.putInt(((LooseMinGiveUpRule) value).getMinPart());
                break;
            case All:
            case Lost:
                break;
            }

            return writeBuffer;
        }

        @Override
        public GiveUpRule read(ByteBuffer readBuffer) {
            int loosingType = (int) readBuffer.get();

            if (loosingType == LoosingType.All.ordinal()) {
                return LooseAllGiveUpRule.INSTANCE;
            } else if (loosingType == LoosingType.Lost.ordinal()) {
                return LooseLostGiveUpRule.INSTANCE;
            } else if (loosingType == LoosingType.MinPart.ordinal()) {
                return LooseMinGiveUpRule.create(readBuffer.getInt());
            }

            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }

    }
}
