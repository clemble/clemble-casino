package com.clemble.casino.integration.game;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.integration.player.Player;
import com.clemble.casino.web.game.GameWebMapping;

public class IntegrationGameSessionPlayerFactory implements GameSessionPlayerFactory {

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationGameSessionPlayerFactory(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public <State extends GameState> GameSessionPlayer<State> construct(Player player, GameConstruction construction) {
        return new IntegrationGameSessionPlayer<State>(player, construction, restTemplate, baseUrl);
    }

    @Override
    public <State extends GameState> GameSessionPlayer<State> construct(Player player, GameSessionKey sessionKey) {
        // Step 1. Generating signed request
        HttpEntity<Void> requestEntity = player.<Void>sign(null);
        // Step 3. Requesting associated Construction
        GameConstruction construction = (GameConstruction) restTemplate.exchange(baseUrl + GameWebMapping.GAME_PREFIX + GameWebMapping.GAME_SESSIONS_CONSTRUCTION,
                HttpMethod.GET, requestEntity, GameConstruction.class, sessionKey.getSession()).getBody();
        // Step 4. Returning IntegrationGameSessionProcessor
        return new IntegrationGameSessionPlayer<State>(player, construction, restTemplate, baseUrl);
    }

}
