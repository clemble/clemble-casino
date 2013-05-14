package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.player.PlayerOperations;

public class IntegrationTicTacToeOperations extends AbstractTicTacToeOperations {

    final private static String ACTION_URL = "/spi/active/action";

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationTicTacToeOperations(String baseUrl,
            RestTemplate restTemplate,
            PlayerOperations playerOperations,
            GameOperations gameOperations,
            GameListenerOperations<TicTacToeState> tableListenerOperations) {
        super(playerOperations, gameOperations, tableListenerOperations);
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    public TicTacToeState perform(TicTacToePlayer player, TicTacToeMove action) {
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayer().getPlayerId()));
        header.add("tableId", String.valueOf(player.getTableId()));
        header.add("sessionId", String.valueOf(player.getSessionId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<TicTacToeMove> requestEntity = new HttpEntity<TicTacToeMove>(action, header);
        // Step 3. Rest template generation
        TicTacToeState updatedState = restTemplate.exchange(baseUrl + ACTION_URL, HttpMethod.POST, requestEntity, TicTacToeState.class).getBody();
        // Step 4. Updating table state
        player.setState(updatedState);
        // Step 5. Returning updated state
        return updatedState;
    }

}
