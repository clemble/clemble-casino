package com.gogomaya.error;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gogomaya.game.Game;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.SessionAware;
import com.gogomaya.player.PlayerAware;

public class GogomayaErrorFormat {

    /**
     * Custom {@link Date} Serializer, used by Jackson, through {@link JsonSerializer} annotation.
     * 
     * @author Anton Oparin
     * 
     */
    public static class GogomayaErrorSerializer extends JsonSerializer<GogomayaError> {

        @Override
        public void serialize(GogomayaError error, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException,
                JsonProcessingException {
            if (error == null)
                return;

            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("code");
            jsonGenerator.writeString(error.getCode());
            jsonGenerator.writeFieldName("description");
            jsonGenerator.writeString(error.getDescription());
            jsonGenerator.writeEndObject();
        }

    }

    public static class GogomayaErrorDeserializer extends JsonDeserializer<GogomayaError> {

        @Override
        public GogomayaError deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String code = null;
            JsonToken token = null;
            do {
                if (jp.getCurrentName() == "code") {
                    code = jp.getValueAsString();
                } else if (jp.getCurrentName() == "description") {
                    jp.getValueAsString();
                }
                token = jp.nextToken();
            } while (token != null && token != JsonToken.END_OBJECT);

            return GogomayaError.forCode(code);
        }
    }

    /**
     * Custom {@link Date} Serializer, used by Jackson, through {@link JsonSerializer} annotation.
     * 
     * @author Anton Oparin
     * 
     */
    public static class GogomayaFailureSerializer extends JsonSerializer<GogomayaFailure> {

        @Override
        public void serialize(GogomayaFailure failure, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException,
                JsonProcessingException {
            if (failure == null)
                return;

            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("code");
            jsonGenerator.writeString(failure.getError().getCode());
            jsonGenerator.writeFieldName("description");
            jsonGenerator.writeString(failure.getError().getDescription());

            if (failure.getPlayerId() != PlayerAware.DEFAULT_PLAYER) {
                jsonGenerator.writeFieldName("player");
                jsonGenerator.writeNumber(failure.getPlayerId());
            }

            if (failure.getSession() != SessionAware.DEFAULT_SESSION) {
                jsonGenerator.writeFieldName("session");
                jsonGenerator.writeStartObject();
                jsonGenerator.writeFieldName("game");
                jsonGenerator.writeString(failure.getSession().getGame().name());
                jsonGenerator.writeFieldName("session");
                jsonGenerator.writeNumber(failure.getSession().getSession());
                jsonGenerator.writeEndObject();
            }

            jsonGenerator.writeEndObject();
        }

    }

    public static class GogomayaFailureDeserializer extends JsonDeserializer<GogomayaFailure> {

        @Override
        public GogomayaFailure deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String code = null;
            JsonToken token = null;
            long playerId = PlayerAware.DEFAULT_PLAYER;
            GameSessionKey session = SessionAware.DEFAULT_SESSION;
            do {
                if (jp.getCurrentName() == "code") {
                    code = jp.getValueAsString();
                } else if (jp.getCurrentName() == "description") {
                    jp.getValueAsString();
                } else if (jp.getCurrentName() == "player") {
                    playerId = jp.getNumberValue().longValue();
                } else if (jp.getCurrentName() == "session") {
                    jp.nextToken();
                    jp.nextToken();
                    if (jp.getCurrentName() == "game") {
                        Game game = Game.valueOf(jp.getValueAsString());
                        jp.nextToken();
                        session = new GameSessionKey(game, jp.getNumberValue().longValue());
                    } else {
                        long sessionIdentifier = jp.getNumberValue().longValue();
                        jp.nextToken();
                        session = new GameSessionKey(Game.valueOf(jp.getValueAsString()), sessionIdentifier);
                    }
                }
                token = jp.nextToken();
            } while (token != null && token != JsonToken.END_OBJECT);

            return new GogomayaFailure(GogomayaError.forCode(code), playerId, session);
        }
    }

}
