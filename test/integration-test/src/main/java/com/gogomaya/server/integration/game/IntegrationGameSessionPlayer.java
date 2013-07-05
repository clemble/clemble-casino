package com.gogomaya.server.integration.game;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.ServerResourse;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.event.client.GameClientEvent;
import com.gogomaya.server.integration.player.Player;

public class IntegrationGameSessionPlayer<State extends GameState> extends AbstractGameSessionPlayer<State> {

    /**
     * Generated 05/07/13
     */
    private static final long serialVersionUID = 2240508744070098170L;

    final private RestTemplate restTemplate;
    final private String baseUrl;

    final private static String ACTION_URL = "/spi/active/action";

    public IntegrationGameSessionPlayer(Player player, GameConstruction construction, RestTemplate restTemplate, String baseUrl) {
        super(player, construction);
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    @SuppressWarnings("unchecked")
    public State perform(ServerResourse resourse, long session, GameClientEvent clientEvent) {
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(clientEvent.getPlayerId()));
        header.add("tableId", String.valueOf(resourse.getTableId()));
        header.add("sessionId", String.valueOf(session));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<ClientEvent> requestEntity = new HttpEntity<ClientEvent>(clientEvent, header);
        // Step 3. Rest template generation
        return (State) restTemplate.exchange(baseUrl + ACTION_URL, HttpMethod.POST, requestEntity, GameState.class).getBody();
    }

}
