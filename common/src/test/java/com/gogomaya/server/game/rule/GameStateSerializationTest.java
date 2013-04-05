package com.gogomaya.server.game.rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.tictactoe.TicTacToePlayerState;
import com.gogomaya.server.game.action.tictactoe.TicTacToeState;

public class GameStateSerializationTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        Collection<TicTacToePlayerState> players = new ArrayList<TicTacToePlayerState>();
        players.add(new TicTacToePlayerState(1L, 50L));
        players.add(new TicTacToePlayerState(2L, 50L));

        TicTacToeState tacToeState = new TicTacToeState(players);
        
        String jsonPresentation = objectMapper.writeValueAsString(tacToeState);
        System.out.println(jsonPresentation);
        TicTacToeState readState = (TicTacToeState) objectMapper.readValue(jsonPresentation, GameState.class);
    }

}
