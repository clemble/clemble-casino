package com.clemble.casino.android.game.service;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.android.AbstractClembleCasinoOperations;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameRequest;
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
    public GameConstruction construct(String player, GameRequest gameRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GameConstruction getConstruct(String player, String session) {
        return restTemplate
            .getForEntity(buildUriById(session, GameWebMapping.GAME_SESSIONS_CONSTRUCTION, "sessionId", session), GameConstruction.class)
            .getBody();
    }

    @Override
    public ClientEvent getResponce(String requester, String session, String player) {
        return restTemplate
            .getForEntity(buildUriById(session, GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES_PLAYER, "sessionId", session, "playerId", player), ClientEvent.class)
            .getBody();
    }

    @Override
    public GameConstruction reply(String player, String session, InvitationResponseEvent gameRequest) {
        return restTemplate
            .postForEntity(buildUriById(session, GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES, "sesssionId", session), gameRequest, GameConstruction.class)
            .getBody();
    }

}
