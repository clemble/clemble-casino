package com.gogomaya.server.integration.game.construction;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.construct.GameRequest;
import com.gogomaya.server.game.event.schedule.InvitationResponseEvent;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.GameSessionPlayerFactory;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.mapping.GameWebMapping;

public class IntegrationGameConstructionOperations<State extends GameState> extends AbstractGameConstructionOperation<State> {

    /**
     * Generated 03/07/13
     */
    private static final long serialVersionUID = 5388660689519677191L;

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationGameConstructionOperations(final String name,
            final String baseUrl,
            final RestTemplate restTemplate,
            final GameSessionPlayerFactory<State> playerFactory) {
        super(name, playerFactory);
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    @Override
    public GameSpecificationOptions getOptions(String name, Player player) {
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        if (player != null)
            header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<GameSpecification> requestEntity = new HttpEntity<GameSpecification>(header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + GameWebMapping.GAME_PREFIX + GameWebMapping.GAME_SPECIFICATION_OPTIONS, HttpMethod.GET, requestEntity,
                GameSpecificationOptions.class, name).getBody();
    }

    public GameConstruction request(Player player, GameRequest gameRequest) {
        checkNotNull(gameRequest);
        // Step 1. Generating signed player request
        HttpEntity<GameRequest> requestEntity = player.<GameRequest> sign(gameRequest);
        // Step 2. Rest template generation
        return (GameConstruction) restTemplate.exchange(baseUrl + GameWebMapping.GAME_PREFIX + GameWebMapping.GAME_CONSTRUCTION_GENERIC, HttpMethod.POST,
                requestEntity, GameConstruction.class, gameRequest.getSpecification().getName().getName()).getBody();
    }

    @Override
    protected void response(Player player, InvitationResponseEvent responseEvent) {
        // Step 1. Generating signed request
        HttpEntity<InvitationResponseEvent> requestEntity = player.<InvitationResponseEvent> sign(responseEvent);
        // Step 2. Rest template generation
        restTemplate.exchange(baseUrl + GameWebMapping.GAME_PREFIX + GameWebMapping.GAME_CONSTRUCTION_RESPONSE, HttpMethod.POST, requestEntity,
                GameConstruction.class, responseEvent.getSession());
    }

}
