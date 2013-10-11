package com.clemble.casino.integration.game.construction;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.configuration.GameSpecificationOptions;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.GameSessionPlayerFactory;
import com.clemble.casino.integration.player.Player;
import com.clemble.casino.web.game.GameWebMapping;

public class IntegrationGameConstructionOperations<State extends GameState> extends AbstractGameConstructionOperation<State> {

    /**
     * Generated 03/07/13
     */
    private static final long serialVersionUID = 5388660689519677191L;

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationGameConstructionOperations(final Game game,
            final String baseUrl,
            final RestTemplate restTemplate,
            final GameSessionPlayerFactory<State> playerFactory) {
        super(game, playerFactory);
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    @Override
    public GameSpecificationOptions getOptions(Game game, Player player) {
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        if (player != null)
            header.add("playerId", String.valueOf(player.getPlayer()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<GameSpecification> requestEntity = new HttpEntity<GameSpecification>(header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + GameWebMapping.GAME_PREFIX + GameWebMapping.GAME_SPECIFICATION_OPTIONS, HttpMethod.GET, requestEntity,
                GameSpecificationOptions.class, game).getBody();
    }

    public GameConstruction request(Player player, GameRequest gameRequest) {
        checkNotNull(gameRequest);
        // Step 1. Generating signed player request
        HttpEntity<GameRequest> requestEntity = player.<GameRequest> sign(gameRequest);
        // Step 2. Rest template generation
        return (GameConstruction) restTemplate.exchange(baseUrl + GameWebMapping.GAME_PREFIX + GameWebMapping.GAME_SESSIONS, HttpMethod.POST, requestEntity,
                GameConstruction.class, gameRequest.getSpecification().getName().getGame()).getBody();
    }

    @Override
    protected void response(Player player, InvitationResponseEvent responseEvent) {
        // Step 1. Generating signed request
        HttpEntity<InvitationResponseEvent> requestEntity = player.<InvitationResponseEvent> sign(responseEvent);
        // Step 2. Rest template generation
        restTemplate.exchange(baseUrl + GameWebMapping.GAME_PREFIX + GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES, HttpMethod.POST, requestEntity,
                GameConstruction.class, responseEvent.getSession().getSession());
    }

    @Override
    public ClientEvent constructionResponse(Player player, String requested, GameSessionKey construction) {
        // Step 1. Generating signed request
        HttpEntity<Void> requestEntity = player.<Void> sign(null);
        // Step 2. Rest template generation
        return restTemplate.exchange(baseUrl + GameWebMapping.GAME_PREFIX + GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES_PLAYER, HttpMethod.GET,
                requestEntity, ClientEvent.class, construction.getSession(), requested).getBody();
    }

}
