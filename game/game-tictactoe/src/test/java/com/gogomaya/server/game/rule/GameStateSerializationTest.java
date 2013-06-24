package com.gogomaya.server.game.rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.game.GamePlayerState;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.game.tictactoe.event.client.TicTacToeSelectCellEvent;
import com.gogomaya.server.json.CustomJacksonAnnotationIntrospector;

@Ignore
public class GameStateSerializationTest {

    private String presentation = "{\"type\":\"ticTacToe\",\"playerIterator\":{\"type\":\"sequential\",\"index\":0,\"players\":[1,2]},\"nextMoves\":[{\"type\":\"select\",\"playerId\":1, \"cell\":{\"row\":-128,\"column\":-128}}],\"madeMoves\":[],\"board\":[[{\"owner\":0,\"firstPlayerBet\":0,\"secondPlayerBet\":0},{\"owner\":0,\"firstPlayerBet\":0,\"secondPlayerBet\":0},{\"owner\":0,\"firstPlayerBet\":0,\"secondPlayerBet\":0}],[{\"owner\":0,\"firstPlayerBet\":0,\"secondPlayerBet\":0},{\"owner\":0,\"firstPlayerBet\":0,\"secondPlayerBet\":0},{\"owner\":0,\"firstPlayerBet\":0,\"secondPlayerBet\":0}],[{\"owner\":0,\"firstPlayerBet\":0,\"secondPlayerBet\":0},{\"owner\":0,\"firstPlayerBet\":0,\"secondPlayerBet\":0},{\"owner\":0,\"firstPlayerBet\":0,\"secondPlayerBet\":0}]],\"activeCell\":null,\"playerStates\":[{\"playerId\":1,\"moneyLeft\":50},{\"playerId\":2,\"moneyLeft\":50}]}";

    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectMapper anotherObjectMapper = new ObjectMapper();

    {
        anotherObjectMapper.setAnnotationIntrospector(new CustomJacksonAnnotationIntrospector());
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        TicTacToeState readState = null;

        Collection<GamePlayerState> players = new ArrayList<GamePlayerState>();
        players.add(new GamePlayerState(1L, 50L));
        players.add(new GamePlayerState(2L, 50L));

        TicTacToeState tacToeState = new TicTacToeState(players);
        tacToeState.addMadeMove(new TicTacToeSelectCellEvent(1L));
        Assert.assertNotNull(tacToeState.getMadeMove(1L));

        String jsonPresentation = objectMapper.writeValueAsString(tacToeState);
        System.out.println(jsonPresentation);
        readState = (TicTacToeState) objectMapper.readValue(jsonPresentation, GameState.class);
        Assert.assertNull(readState.getMadeMove(1L));

        jsonPresentation = anotherObjectMapper.writeValueAsString(tacToeState);
        System.out.println(jsonPresentation);
        readState = (TicTacToeState) anotherObjectMapper.readValue(jsonPresentation, GameState.class);

        Assert.assertNotNull(readState.getMadeMove(1L));

    }

    @Test
    public void testState() throws JsonParseException, JsonMappingException, IOException {
        TicTacToeState state = (TicTacToeState) anotherObjectMapper.readValue(presentation, GameState.class);

        String jsonPresentation = anotherObjectMapper.writeValueAsString(state);
        System.out.println(jsonPresentation);
        anotherObjectMapper.readValue(jsonPresentation, GameState.class);
    }
}
