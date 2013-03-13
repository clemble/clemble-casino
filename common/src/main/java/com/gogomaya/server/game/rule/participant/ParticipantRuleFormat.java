package com.gogomaya.server.game.rule.participant;

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

public class ParticipantRuleFormat {

    final public static String PARTICIPANT_TYPE_TOKEN = "participantType";
    final public static String MATCH_TYPE_TOKEN = "matchType";
    final public static String PRIVACY_TYPE_TOKEN = "privacyType";
    final public static String NUMBER_OF_PLAYERS_TOKEN = "players";
    final public static String MAX_PLAYERS_TOKEN = "maxPlayers";
    final public static String MIN_PLAYERS_TOKEN = "minPlayers";

    public static class CustomParticipantRuleSerializer extends JsonSerializer<ParticipantRule> {

        @Override
        public void serialize(ParticipantRule value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeFieldName(PARTICIPANT_TYPE_TOKEN);
            jgen.writeString(value.getParticipantType().name());
            jgen.writeFieldName(MATCH_TYPE_TOKEN);
            jgen.writeString(value.getMatchType().name());
            jgen.writeFieldName(PRIVACY_TYPE_TOKEN);
            jgen.writeString(value.getPrivacyType().name());
            if (value instanceof FixedParticipantRule) {
                jgen.writeFieldName(NUMBER_OF_PLAYERS_TOKEN);
                jgen.writeNumber(((FixedParticipantRule) value).getNumberOfParticipants());
            } else if (value instanceof LimitedParticipantRule) {
                jgen.writeFieldName(MIN_PLAYERS_TOKEN);
                jgen.writeNumber(((LimitedParticipantRule) value).getMinPlayers());
                jgen.writeFieldName(MAX_PLAYERS_TOKEN);
                jgen.writeNumber(((LimitedParticipantRule) value).getMaxPlayers());
            }
            jgen.writeEndObject();
        }

    }

    public static class CustomParticipantRuleDeserializer extends JsonDeserializer<ParticipantRule> {

        @Override
        public ParticipantRule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ParticipantType participantType = null;
            ParticipantMatchType matchType = ParticipantMatchType.Automatic;
            ParticipantPrivacyType privacyType = ParticipantPrivacyType.Public;
            int minParticipants = 0;
            int maxParticipants = 0;

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String currentName = jp.getCurrentName();
                if (MATCH_TYPE_TOKEN.equals(currentName)) {
                    matchType = ParticipantMatchType.valueOf(jp.nextTextValue());
                } else if (PRIVACY_TYPE_TOKEN.equals(currentName)) {
                    privacyType = ParticipantPrivacyType.valueOf(jp.nextTextValue());
                } else if (PARTICIPANT_TYPE_TOKEN.equals(currentName)) {
                    participantType = ParticipantType.valueOf(jp.nextTextValue());
                } else if (MIN_PLAYERS_TOKEN.equals(currentName) || NUMBER_OF_PLAYERS_TOKEN.equals(currentName)) {
                    minParticipants = jp.nextIntValue(0);
                } else if (MAX_PLAYERS_TOKEN.equals(currentName)) {
                    maxParticipants = jp.nextIntValue(0);
                } else {
                    throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
                }
            }

            switch (participantType) {
            case Fixed:
                return FixedParticipantRule.create(matchType, privacyType, minParticipants);
            case Limited:
                return LimitedParticipantRule.create(matchType, privacyType, minParticipants, maxParticipants);
            }
            throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
        }

    }

    public static class CustomParticipantRuleType extends AbstractImmutableUserType<ParticipantRule> {

        final private int[] TYPES = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER };

        @Override
        public int[] sqlTypes() {
            return TYPES;
        }

        @Override
        public Class<?> returnedClass() {
            return ParticipantRule.class;
        }

        @Override
        public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
            ParticipantType participantType = ParticipantType.valueOf(rs.getString(names[0]));
            ParticipantMatchType matchType = ParticipantMatchType.valueOf(rs.getString(names[1]));
            ParticipantPrivacyType privacyType = ParticipantPrivacyType.valueOf(rs.getString(names[2]));

            switch (participantType) {
            case Fixed:
                return FixedParticipantRule.create(matchType, privacyType, rs.getInt(names[3]));
            case Limited:
                return LimitedParticipantRule.create(matchType, privacyType, rs.getInt(names[3]), rs.getInt(names[4]));
            }
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            ParticipantRule participantRule = (ParticipantRule) value;
            st.setString(index++, participantRule.getParticipantType().name());
            st.setString(index++, participantRule.getMatchType().name());
            st.setString(index++, participantRule.getPrivacyType().name());
            switch (participantRule.getParticipantType()) {
            case Fixed:
                st.setInt(index++, ((FixedParticipantRule) participantRule).getNumberOfParticipants());
                st.setInt(index++, 0);
                break;
            case Limited:
                st.setInt(index++, ((LimitedParticipantRule) participantRule).getMinPlayers());
                st.setInt(index++, ((LimitedParticipantRule) participantRule).getMaxPlayers());
                break;
            }
        }

    }

    public static class CustomParticipantRuleByteBufferStream implements ByteBufferStream<ParticipantRule> {

        @Override
        public ByteBuffer write(ParticipantRule value, ByteBuffer writeBuffer) {
            writeBuffer.put((byte) value.getParticipantType().ordinal());
            writeBuffer.put((byte) value.getMatchType().ordinal());
            writeBuffer.put((byte) value.getPrivacyType().ordinal());

            switch (value.getParticipantType()) {
            case Fixed:
                writeBuffer.putInt(((FixedParticipantRule) value).getNumberOfParticipants());
                break;
            case Limited:
                writeBuffer.putInt(((LimitedParticipantRule) value).getMinPlayers());
                writeBuffer.putInt(((LimitedParticipantRule) value).getMaxPlayers());
                break;
            default:
                throw GogomayaException.create(GogomayaError.ServerCriticalError);
            }

            return writeBuffer;
        }

        @Override
        public ParticipantRule read(ByteBuffer readBuffer) {
            byte participant = readBuffer.get();
            ParticipantType participantType = participant == ParticipantType.Fixed.ordinal() ? ParticipantType.Fixed : participant == ParticipantType.Limited
                    .ordinal() ? ParticipantType.Limited : null;

            byte match = readBuffer.get();
            ParticipantMatchType matchType = match == ParticipantMatchType.Automatic.ordinal() ? ParticipantMatchType.Automatic
                    : match == ParticipantMatchType.Manual.ordinal() ? ParticipantMatchType.Manual : null;

            byte privacy = readBuffer.get();
            ParticipantPrivacyType privacyType = privacy == ParticipantPrivacyType.Private.ordinal() ? ParticipantPrivacyType.Private
                    : privacy == ParticipantPrivacyType.Public.ordinal() ? ParticipantPrivacyType.Public : null;

            switch (participantType) {
            case Fixed:
                return FixedParticipantRule.create(matchType, privacyType, readBuffer.getInt());
            case Limited:
                return LimitedParticipantRule.create(matchType, privacyType, readBuffer.getInt(), readBuffer.getInt());
            default:
                throw GogomayaException.create(GogomayaError.ServerCriticalError);

            }
        }

    }
}
