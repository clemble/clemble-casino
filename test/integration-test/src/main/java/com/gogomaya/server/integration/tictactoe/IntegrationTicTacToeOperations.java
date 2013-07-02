package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.event.schedule.InvitationResponceEvent;
import com.gogomaya.server.game.tictactoe.TicTacToeState;

public class IntegrationTicTacToeOperations extends AbstractTicTacToeOperations {

    final private static String ACTION_URL = "/spi/active/action";
    final private static String CONSTRUCTION_URL = "/spi/active/session/registration";

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationTicTacToeOperations(final String baseUrl, final RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    public TicTacToeState perform(TicTacToePlayer player, ClientEvent action) {
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayer().getPlayerId()));
        header.add("tableId", String.valueOf(player.getTableId()));
        header.add("sessionId", String.valueOf(player.getSession()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<ClientEvent> requestEntity = new HttpEntity<ClientEvent>(action, header);
        // Step 3. Rest template generation
        TicTacToeState updatedState = restTemplate.exchange(baseUrl + ACTION_URL, HttpMethod.POST, requestEntity, TicTacToeState.class).getBody();
        // Step 4. Updating table state
        player.setState(updatedState);
        // Step 5. Returning updated state
        return updatedState;
    }

    @Override
    protected void responce(InvitationResponceEvent responceEvent) {
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(responceEvent.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<InvitationResponceEvent> requestEntity = new HttpEntity<InvitationResponceEvent>(responceEvent, header);
        // Step 3. Rest template generation
        restTemplate.exchange(baseUrl + CONSTRUCTION_URL, HttpMethod.POST, requestEntity, GameConstruction.class);
    }

}
