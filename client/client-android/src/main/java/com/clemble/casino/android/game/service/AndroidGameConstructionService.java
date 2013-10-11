package com.clemble.casino.android.game.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.service.GameConstructionService;
import com.clemble.casino.web.game.GameWebMapping;
import com.clemble.casino.client.service.RestClientService;

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
    public GameConstruction getConstruct(String player, long session) {
        return restService.getForEntity(GameWebMapping.GAME_SESSIONS_CONSTRUCTION, GameConstruction.class, session);
    }

    @Override
    public ClientEvent getResponce(String requester, long session, String player) {
        return restService.getForEntity(GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES_PLAYER, ClientEvent.class, session, player);
    }

    @Override
    public GameConstruction reply(String player, long sessionId, InvitationResponseEvent gameRequest) {
        return restService.postForEntity(GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES, gameRequest, GameConstruction.class, sessionId);
    }

    @Override
    public String getServer(String playerId, long sessionId) {
        return restService.getForEntity(GameWebMapping.GAME_SESSIONS_SERVER, String.class, sessionId);
    }

}
