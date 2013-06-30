package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;

public class IntegrationGameOperations<State extends GameState> extends AbstractGameOperation<State> {

    final private static String CREATE_URL = "/spi/active/session";
    final private static String OPTIONS_URL = "/spi/active/options";

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationGameOperations(final String baseUrl, final RestTemplate restTemplate, final GamePlayerFactory<State> playerFactory, final PlayerOperations playerOperations) {
        super(playerOperations, playerFactory);
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    @Override
    public GameSpecificationOptions getOptions() {
        return getOptions(null);
    }

    @Override
    public GameSpecificationOptions getOptions(Player player) {
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        if (player != null)
            header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<GameSpecification> requestEntity = new HttpEntity<GameSpecification>(header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + OPTIONS_URL, HttpMethod.GET, requestEntity, GameSpecificationOptions.class).getBody();
    }

    public GameConstruction request(Player player, GameSpecification gameSpecification) {
        gameSpecification = checkNotNull(gameSpecification);
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<GameSpecification> requestEntity = new HttpEntity<GameSpecification>(gameSpecification, header);
        // Step 3. Rest template generation
        return (GameConstruction) restTemplate.exchange(baseUrl + CREATE_URL, HttpMethod.POST, requestEntity, GameConstruction.class).getBody();
    }

}
