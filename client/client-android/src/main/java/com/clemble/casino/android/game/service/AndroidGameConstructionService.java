package com.clemble.casino.android.game.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.client.service.RestClientService;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.service.GameConstructionService;
import com.clemble.casino.web.game.GameWebMapping;

public class AndroidGameConstructionService implements GameConstructionService {

    final private RestClientService restService;

    public AndroidGameConstructionService(RestClientService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    public GameConstruction construct(String player, GameRequest gameRequest) {
        return restService.postForEntity(GameWebMapping.GAME_SESSIONS, gameRequest, GameConstruction.class);
    }

    @Override
    public GameConstruction getConstruct(String player, String session) {
        return restService.getForEntity(GameWebMapping.GAME_SESSIONS_CONSTRUCTION, GameConstruction.class, session);
    }

    @Override
    public ClientEvent getResponce(String requester, String session, String player) {
        return restService.getForEntity(GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES_PLAYER, ClientEvent.class, session, player);
    }

    @Override
    public GameConstruction reply(String player, String sessionId, InvitationResponseEvent gameRequest) {
        return restService.postForEntity(GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES, gameRequest, GameConstruction.class, sessionId);
    }

}
