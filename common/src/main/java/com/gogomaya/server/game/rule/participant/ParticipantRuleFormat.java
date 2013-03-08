package com.gogomaya.server.game.rule.participant;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;

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
}
