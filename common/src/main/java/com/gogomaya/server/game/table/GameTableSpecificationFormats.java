package com.gogomaya.server.game.table;

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
import com.gogomaya.server.game.table.rule.GameTableMatchRule;
import com.gogomaya.server.game.table.rule.GameTablePlayerNumberRule;
import com.gogomaya.server.game.table.rule.GameTablePrivacyRule;
import com.gogomaya.server.hibernate.ImmutableHibernateType;

public class GameTableSpecificationFormats {

    final public static String MATCH_TYPE_TOKEN = "matchRule";
    final public static String PRIVACY_TYPE_TOKEN = "privacyRule";
    final public static String MAX_PLAYERS_TOKEN = "maxPlayers";
    final public static String MIN_PLAYERS_TOKEN = "minPlayers";

    public static class GameTableSpecificationJsonSerializer extends JsonSerializer<GameTableSpecification> {

        @Override
        public void serialize(GameTableSpecification value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeFieldName(MATCH_TYPE_TOKEN);
            jgen.writeString(value.getMatchRule().name());
            jgen.writeFieldName(PRIVACY_TYPE_TOKEN);
            jgen.writeString(value.getPrivacyRule().name());
            jgen.writeFieldName(MIN_PLAYERS_TOKEN);
            jgen.writeNumber(value.getNumberRule().getMinPlayers());
            jgen.writeFieldName(MAX_PLAYERS_TOKEN);
            jgen.writeNumber(value.getNumberRule().getMaxPlayers());
            jgen.writeEndObject();
        }

    }

    public static class GameTableSpecificationJsonDeserializer extends JsonDeserializer<GameTableSpecification> {

        @Override
        public GameTableSpecification deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            GameTableMatchRule matchRule = GameTableMatchRule.automatic;
            GameTablePrivacyRule privacyRule = GameTablePrivacyRule.all;
            int minPlayers = 0;
            int maxPlayers = 0;

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String currentName = jp.getCurrentName();
                if (MATCH_TYPE_TOKEN.equals(currentName)) {
                    matchRule = GameTableMatchRule.valueOf(jp.nextTextValue());
                } else if (PRIVACY_TYPE_TOKEN.equals(currentName)) {
                    privacyRule = GameTablePrivacyRule.valueOf(jp.nextTextValue());
                } else if (MIN_PLAYERS_TOKEN.equals(currentName)) {
                    minPlayers = jp.nextIntValue(0);
                } else if (MAX_PLAYERS_TOKEN.equals(currentName)) {
                    maxPlayers = jp.nextIntValue(0);
                } else {
                    throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
                }
            }

            return GameTableSpecification.create(matchRule, privacyRule, GameTablePlayerNumberRule.create(minPlayers, maxPlayers));
        }

    }

    public static class GameTableSpecificationHybernateType extends ImmutableHibernateType<GameTableSpecification> {

        final private int[] TYPES = { Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER };

        @Override
        public int[] sqlTypes() {
            return TYPES;
        }

        @Override
        public Class<?> returnedClass() {
            return GameTableSpecification.class;
        }

        @Override
        public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {

            GameTableMatchRule matchType = GameTableMatchRule.valueOf(rs.getString(names[0]));
            GameTablePrivacyRule privacyType = GameTablePrivacyRule.valueOf(rs.getString(names[1]));

            return GameTableSpecification.create(matchType, privacyType, GameTablePlayerNumberRule.create(rs.getInt(names[2]), rs.getInt(names[3])));
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            GameTableSpecification participantRule = value != null ? (GameTableSpecification) value : GameTableSpecification.DEFAULT;

            st.setString(index++, participantRule.getMatchRule().name());
            st.setString(index++, participantRule.getPrivacyRule().name());
            st.setInt(index++, participantRule.getNumberRule().getMinPlayers());
            st.setInt(index++, participantRule.getNumberRule().getMaxPlayers());
        }

    }

    public static class GameTableSpecificationByteBufferStream implements ByteBufferStream<GameTableSpecification> {

        @Override
        public ByteBuffer write(GameTableSpecification value, ByteBuffer writeBuffer) {
            writeBuffer.put((byte) value.getMatchRule().ordinal());
            writeBuffer.put((byte) value.getPrivacyRule().ordinal());
            writeBuffer.putInt(value.getNumberRule().getMinPlayers());
            writeBuffer.putInt(value.getNumberRule().getMaxPlayers());
            return writeBuffer;
        }

        @Override
        public GameTableSpecification read(ByteBuffer readBuffer) {
            byte match = readBuffer.get();
            GameTableMatchRule matchType = match == GameTableMatchRule.automatic.ordinal() ? GameTableMatchRule.automatic : match == GameTableMatchRule.manual
                    .ordinal() ? GameTableMatchRule.manual : null;

            byte privacy = readBuffer.get();
            GameTablePrivacyRule privacyType = privacy == GameTablePrivacyRule.players.ordinal() ? GameTablePrivacyRule.players
                    : privacy == GameTablePrivacyRule.all.ordinal() ? GameTablePrivacyRule.all : null;

            return GameTableSpecification.create(matchType, privacyType, GameTablePlayerNumberRule.create(readBuffer.getInt(), readBuffer.getInt()));
        }

    }
}
