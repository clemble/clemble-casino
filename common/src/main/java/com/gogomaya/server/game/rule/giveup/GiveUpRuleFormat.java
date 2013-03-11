package com.gogomaya.server.game.rule.giveup;

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
            st.setString(index++, ((GiveUpRule) value).getLoosingType().name());
            st.setInt(index, value instanceof LooseMinGiveUpRule ? ((LooseMinGiveUpRule) value).getMinPart() : 0);
        }

    }
}
