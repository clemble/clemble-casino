package com.clemble.casino.error;

import java.io.IOException;
import java.util.Date;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ClembleCasinoErrorFormat {

    /**
     * Custom {@link Date} Serializer, used by Jackson, through {@link JsonSerializer} annotation.
     * 
     * @author Anton Oparin
     * 
     */
    public static class ClembleCasinoErrorSerializer extends JsonSerializer<ClembleCasinoError> {

        @Override
        public void serialize(ClembleCasinoError error, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException,
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

    public static class ClembleCasinoErrorDeserializer extends JsonDeserializer<ClembleCasinoError> {

        @Override
        public ClembleCasinoError deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
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

            return ClembleCasinoError.forCode(code);
        }
    }

    /**
     * Custom {@link Date} Serializer, used by Jackson, through {@link JsonSerializer} annotation.
     * 
     * @author Anton Oparin
     * 
     */
    public static class ClembleCasinoFailureSerializer extends JsonSerializer<ClembleCasinoFailure> {

        @Override
        public void serialize(ClembleCasinoFailure failure, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException,
                JsonProcessingException {
            if (failure == null)
                return;

            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("code");
            jsonGenerator.writeString(failure.getError().getCode());
            jsonGenerator.writeFieldName("description");
            jsonGenerator.writeString(failure.getError().getDescription());

            if (failure.getPlayer() != PlayerAware.DEFAULT_PLAYER) {
                jsonGenerator.writeFieldName("player");
                jsonGenerator.writeNumber(failure.getPlayer());
            }

            if (failure.getSession() != GameSessionAware.DEFAULT_SESSION) {
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

    public static class ClembleCasinoFailureDeserializer extends JsonDeserializer<ClembleCasinoFailure> {

        @Override
        public ClembleCasinoFailure deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String code = null;
            JsonToken token = null;
            String player = PlayerAware.DEFAULT_PLAYER;
            GameSessionKey session = GameSessionAware.DEFAULT_SESSION;
            do {
                if (jp.getCurrentName() == "code") {
                    code = jp.getValueAsString();
                } else if (jp.getCurrentName() == "description") {
                    jp.getValueAsString();
                } else if (jp.getCurrentName() == "player") {
                    player = jp.getValueAsString();
                } else if (jp.getCurrentName() == "session") {
                    jp.nextToken();
                    jp.nextToken();
                    if (jp.getCurrentName() == "game") {
                        Game game = Game.valueOf(jp.getValueAsString());
                        jp.nextToken();
                        session = new GameSessionKey(game, jp.getValueAsString());
                    } else {
                        String sessionIdentifier = jp.getValueAsString();
                        jp.nextToken();
                        session = new GameSessionKey(Game.valueOf(jp.getValueAsString()), sessionIdentifier);
                    }
                }
                token = jp.nextToken();
            } while (token != null && token != JsonToken.END_OBJECT);

            return new ClembleCasinoFailure(ClembleCasinoError.forCode(code), player, session);
        }
    }

}
