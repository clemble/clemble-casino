package com.clemble.casino.android.game.service;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.android.AbstractClembleCasinoOperations;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.PlayerGameConstructionRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.service.GameConstructionService;
import com.clemble.casino.web.game.GameWebMapping;

public class AndroidGameConstructionService<T extends GameState> extends AbstractClembleCasinoOperations implements GameConstructionService {

    final private RestTemplate restTemplate;

    public AndroidGameConstructionService(RestTemplate restTemplate, ServerRegistry apiBase) {
        super(apiBase);
        this.restTemplate = restTemplate;
    }

    @Override
    public GameConstruction construct(PlayerGameConstructionRequest gameRequest) {
        return restTemplate
                .postForEntity(buildUriWith(GameWebMapping.GAME_SESSIONS), gameRequest, GameConstruction.class)
                .getBody();
    }

    @Override
    public GameConstruction getConstruct(Game game, String session) {
        return restTemplate
            .getForEntity(buildUriWith(GameWebMapping.GAME_CONSTRUCTION, game, session), GameConstruction.class)
            .getBody();
    }

    @Override
    public ClientEvent getResponce(Game game, String session, String player) {
        return restTemplate
            .getForEntity(buildUriWith(GameWebMapping.GAME_CONSTRUCTION_RESPONSES_PLAYER, game, session, player), ClientEvent.class)
            .getBody();
    }

    @Override
    public GameConstruction reply(Game game, String session, InvitationResponseEvent gameRequest) {
        return restTemplate
            .postForEntity(buildUriWith(GameWebMapping.GAME_CONSTRUCTION_RESPONSES, game, session), gameRequest, GameConstruction.class)
            .getBody();
    }

}
