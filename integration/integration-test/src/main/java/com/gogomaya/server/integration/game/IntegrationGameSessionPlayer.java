package com.gogomaya.server.integration.game;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.GameState;
import com.gogomaya.game.ServerResourse;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.event.client.GameClientEvent;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.web.game.GameWebMapping;

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
    public State perform(Player player, ServerResourse resourse, GameSessionKey session, GameClientEvent clientEvent) {
        // Step 1. Generating signed request
        HttpEntity<ClientEvent> requestEntity = player.<ClientEvent> signGame(session, clientEvent);
        // Step 2. Rest template generation
        return (State) restTemplate.exchange(baseUrl + GameWebMapping.GAME_PREFIX + GameWebMapping.GAME_SESSIONS_ACTIONS, HttpMethod.POST, requestEntity,
                GameState.class, session.getSession()).getBody();
    }

}
