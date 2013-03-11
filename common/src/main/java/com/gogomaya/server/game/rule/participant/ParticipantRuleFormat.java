package com.gogomaya.server.game.rule.participant;

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

public class ParticipantRuleFormat {

    final public static SerializableString PARTICIPANT_TYPE_TOKEN = new SerializedString("participantType");
    final public static SerializableString MATCH_TYPE_TOKEN = new SerializedString("matchType");
    final public static SerializableString PRIVACY_TYPE_TOKEN = new SerializedString("privacyType");
    final public static SerializableString NUMBER_OF_PLAYERS_TOKEN = new SerializedString("players");
    final public static SerializableString MAX_PLAYERS_TOKEN = new SerializedString("maxPlayers");
    final public static SerializableString MIN_PLAYERS_TOKEN = new SerializedString("minPlayers");

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
            if (!jp.nextFieldName(PARTICIPANT_TYPE_TOKEN))
                throw GogomayaException.create(GogomayaError.ClientJsonFormatError);

            ParticipantType participantType = ParticipantType.valueOf(jp.nextTextValue());
            ParticipantMatchType matchType = jp.nextFieldName(MATCH_TYPE_TOKEN) ? ParticipantMatchType.valueOf(jp.nextTextValue())
                    : ParticipantMatchType.Automatic;
            ParticipantPrivacyType privacyType = jp.nextFieldName(PRIVACY_TYPE_TOKEN) ? ParticipantPrivacyType.valueOf(jp.nextTextValue())
                    : ParticipantPrivacyType.Public;

            switch (participantType) {
            case Fixed:
                if (!jp.nextFieldName(NUMBER_OF_PLAYERS_TOKEN))
                    throw GogomayaException.create(GogomayaError.ClientJsonFormatError);
                return FixedParticipantRule.create(matchType, privacyType, jp.nextIntValue(0));
            case Limited:
                if (!jp.nextFieldName(MIN_PLAYERS_TOKEN))
                    throw GogomayaException.create(GogomayaError.ClientJsonFormatError);
                int minParticipants = jp.nextIntValue(0);
                if (!jp.nextFieldName(MAX_PLAYERS_TOKEN))
                    throw GogomayaException.create(GogomayaError.ClientJsonFormatError);
                return LimitedParticipantRule.create(matchType, privacyType, minParticipants, jp.nextIntValue(0));
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
}
