package com.gogomaya.server.game.rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.game.GameAccount;
import com.gogomaya.server.game.GamePlayerAccount;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.SequentialPlayerIterator;
import com.gogomaya.server.game.cell.Cell;
import com.gogomaya.server.game.event.client.generic.SelectCellEvent;
import com.gogomaya.server.json.CustomJacksonAnnotationIntrospector;
import com.gogomaya.server.tictactoe.PicPacPoeState;

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
        PicPacPoeState readState = null;

        Collection<GamePlayerAccount> players = new ArrayList<GamePlayerAccount>();
        players.add(new GamePlayerAccount(1L, 50L));
        players.add(new GamePlayerAccount(2L, 50L));

        PicPacPoeState tacToeState = new PicPacPoeState(new GameAccount(players), new SequentialPlayerIterator(players));
        tacToeState.process(new SelectCellEvent(1L, Cell.create(0, 0)));
        Assert.assertNotNull(tacToeState.getActionLatch().fetchAction(1L));

        String jsonPresentation = objectMapper.writeValueAsString(tacToeState);
        System.out.println(jsonPresentation);
        readState = (PicPacPoeState) objectMapper.readValue(jsonPresentation, GameState.class);
        Assert.assertFalse(readState.getActionLatch().acted(1L));

        jsonPresentation = anotherObjectMapper.writeValueAsString(tacToeState);
        System.out.println(jsonPresentation);
        readState = (PicPacPoeState) anotherObjectMapper.readValue(jsonPresentation, GameState.class);

        Assert.assertTrue(readState.getActionLatch().acted(1L));

    }

    @Test
    public void testState() throws JsonParseException, JsonMappingException, IOException {
        PicPacPoeState state = (PicPacPoeState) anotherObjectMapper.readValue(presentation, GameState.class);

        String jsonPresentation = anotherObjectMapper.writeValueAsString(state);
        System.out.println(jsonPresentation);
        anotherObjectMapper.readValue(jsonPresentation, GameState.class);
    }
}
