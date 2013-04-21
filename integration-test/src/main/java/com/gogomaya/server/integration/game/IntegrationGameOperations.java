package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.integration.player.Player;

public class IntegrationGameOperations implements GameOperations {

    final private static String CREATE_URL = "/spi/active/session";
    final private static String OPTIONS_URL = "/spi/active/options";

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationGameOperations(final String baseUrl, final RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    @Override
    public SelectSpecificationOptions getOptions() {
        return getOptions(null);
    }

    @Override
    public SelectSpecificationOptions getOptions(Player player) {
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        if (player != null)
            header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<GameSpecification> requestEntity = new HttpEntity<GameSpecification>(header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + OPTIONS_URL, HttpMethod.GET, requestEntity, SelectSpecificationOptions.class).getBody();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GameTable<?>> T start(Player player, GameSpecification gameSpecification) {
        gameSpecification = checkNotNull(gameSpecification);
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<GameSpecification> requestEntity = new HttpEntity<GameSpecification>(gameSpecification, header);
        // Step 3. Rest template generation
        return (T) restTemplate.exchange(baseUrl + CREATE_URL, HttpMethod.POST, requestEntity, GameTable.class).getBody();
    }

}
