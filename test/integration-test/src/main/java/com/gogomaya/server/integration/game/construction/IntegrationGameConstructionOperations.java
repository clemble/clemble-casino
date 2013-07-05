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
import com.gogomaya.server.game.event.schedule.InvitationResponceEvent;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.GameSessionPlayerFactory;
import com.gogomaya.server.integration.player.Player;

public class IntegrationGameConstructionOperations<State extends GameState> extends AbstractGameConstructionOperation<State> {

    /**
     * Generated 03/07/13
     */
    private static final long serialVersionUID = 5388660689519677191L;

    final private static String CREATE_URL = "/spi/active/constuct";
    final private static String CONSTRUCTION_URL = "/spi/active/constuct/responce";
    final private static String OPTIONS_URL = "/spi/active/options";

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationGameConstructionOperations(final String name, final String baseUrl, final RestTemplate restTemplate,
            final GameSessionPlayerFactory<State> playerFactory) {
        super(name, playerFactory);
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

    public GameConstruction request(Player player, GameRequest gameRequest) {
        checkNotNull(gameRequest);
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<GameRequest> requestEntity = new HttpEntity<GameRequest>(gameRequest, header);
        // Step 3. Rest template generation
        return (GameConstruction) restTemplate.exchange(baseUrl + CREATE_URL, HttpMethod.POST, requestEntity, GameConstruction.class).getBody();
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
