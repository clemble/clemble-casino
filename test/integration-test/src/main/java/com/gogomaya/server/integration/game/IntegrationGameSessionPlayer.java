package com.gogomaya.server.integration.game;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.ServerResourse;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.event.client.GameClientEvent;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.mapping.GameWebMapping;

public class IntegrationGameSessionPlayer<State extends GameState> extends AbstractGameSessionPlayer<State> {

    /**
     * Generated 05/07/13
     */
    private static final long serialVersionUID = 2240508744070098170L;

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationGameSessionPlayer(Player player, GameConstruction construction, RestTemplate restTemplate, String baseUrl) {
        super(player, construction);
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    @SuppressWarnings("unchecked")
    public State perform(Player player, ServerResourse resourse, long session, GameClientEvent clientEvent) {
        // Step 1. Generating signed request
        HttpEntity<ClientEvent> requestEntity = player.<ClientEvent> signGame(session, resourse.getTableId(), clientEvent);
        // Step 2. Rest template generation
        return (State) restTemplate.exchange(baseUrl + GameWebMapping.GAME_PREFIX + GameWebMapping.GAME_SESSION_ACTIONS, HttpMethod.POST, requestEntity,
                GameState.class, session).getBody();
    }

}
