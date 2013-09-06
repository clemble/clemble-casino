package com.gogomaya.android.game.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.gogomaya.client.service.RestClientService;
import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.event.schedule.InvitationResponseEvent;
import com.gogomaya.game.service.GameConstructionService;
import com.gogomaya.web.game.GameWebMapping;

public class AndroidGameConstructionService implements GameConstructionService {

    final private RestClientService restService;

    public AndroidGameConstructionService(RestClientService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    public GameConstruction construct(long playerId, GameRequest gameRequest) {
        return restService.postForEntity(GameWebMapping.GAME_SESSIONS, gameRequest, GameConstruction.class);
    }

    @Override
    public GameConstruction getConstruct(long playerId, long session) {
        return restService.getForEntity(GameWebMapping.GAME_SESSIONS_CONSTRUCTION, GameConstruction.class, session);
    }

    @Override
    public ClientEvent getResponce(long requester, long session, long player) {
        return restService.getForEntity(GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES_PLAYER, ClientEvent.class, session, player);
    }

    @Override
    public GameConstruction invitationResponsed(long playerId, long sessionId, InvitationResponseEvent gameRequest) {
        return restService.postForEntity(GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES, gameRequest, GameConstruction.class, sessionId);
    }

}
