package com.gogomaya.server.game.table.rule;

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
            PlayerMatchRule matchRule = PlayerMatchRule.Automatic;
            PlayerPrivacyRule privacyRule = PlayerPrivacyRule.Public;
            int minPlayers = 0;
            int maxPlayers = 0;

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String currentName = jp.getCurrentName();
                if (MATCH_TYPE_TOKEN.equals(currentName)) {
                    matchRule = PlayerMatchRule.valueOf(jp.nextTextValue());
                } else if (PRIVACY_TYPE_TOKEN.equals(currentName)) {
                    privacyRule = PlayerPrivacyRule.valueOf(jp.nextTextValue());
                } else if (MIN_PLAYERS_TOKEN.equals(currentName)) {
                    minPlayers = jp.nextIntValue(0);
                } else if (MAX_PLAYERS_TOKEN.equals(currentName)) {
                    maxPlayers = jp.nextIntValue(0);
                } else {
                    throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
                }
            }

            return GameTableSpecification.create(matchRule, privacyRule, PlayerNumberRule.create(minPlayers, maxPlayers));
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

            PlayerMatchRule matchType = PlayerMatchRule.valueOf(rs.getString(names[1]));
            PlayerPrivacyRule privacyType = PlayerPrivacyRule.valueOf(rs.getString(names[2]));

            return GameTableSpecification.create(matchType, privacyType, PlayerNumberRule.create(rs.getInt(names[3]), rs.getInt(names[4])));
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            GameTableSpecification participantRule = value != null ? (GameTableSpecification) value : GameTableSpecification.DEFAULT_TABLE_SPECIFICATION;

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
            PlayerMatchRule matchType = match == PlayerMatchRule.Automatic.ordinal() ? PlayerMatchRule.Automatic
                    : match == PlayerMatchRule.Manual.ordinal() ? PlayerMatchRule.Manual : null;

            byte privacy = readBuffer.get();
            PlayerPrivacyRule privacyType = privacy == PlayerPrivacyRule.Private.ordinal() ? PlayerPrivacyRule.Private : privacy == PlayerPrivacyRule.Public
                    .ordinal() ? PlayerPrivacyRule.Public : null;

            return GameTableSpecification.create(matchType, privacyType, PlayerNumberRule.create(readBuffer.getInt(), readBuffer.getInt()));
        }

    }
}
