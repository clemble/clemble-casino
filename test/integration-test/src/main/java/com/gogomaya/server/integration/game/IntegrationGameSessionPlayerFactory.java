package com.gogomaya.server.integration.game;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.integration.player.Player;

public class IntegrationGameSessionPlayerFactory<State extends GameState> implements GameSessionPlayerFactory<State> {

    /**
     * Generated 04/07/13
     */
    private static final long serialVersionUID = -7652085755416835994L;

    final private String CONSTRUCTION_URL = "/spi/active/constuct/";

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationGameSessionPlayerFactory(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GameSessionPlayer<State> construct(Player player, GameConstruction construction) {
        return new IntegrationGameSessionPlayer<State>(player, construction, restTemplate, baseUrl);
    }

    @Override
    public GameSessionPlayer<State> construct(Player player, long constructionId) {
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(null, header);
        // Step 3. Requesting associated Construction
        GameConstruction construction = (GameConstruction) restTemplate.exchange(baseUrl + CONSTRUCTION_URL + constructionId, HttpMethod.GET, requestEntity, GameConstruction.class).getBody();
        // Step 4. Returning IntegrationGameSessionProcessor
        return new IntegrationGameSessionPlayer<State>(player, construction, restTemplate, baseUrl);
    }

}
