package com.gogomaya.client.game.service;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.event.schedule.InvitationResponseEvent;
import com.gogomaya.game.service.GameConstructionService;

public class SimpleGameConstructionOperations implements GameConstructionOperations {

    final private long playerId;
    final private GameConstructionService constructionService;

    public SimpleGameConstructionOperations(long playerId, GameConstructionService constructionService) {
        this.playerId = playerId;
        this.constructionService = constructionService;
    }

    @Override
    public GameConstruction construct(GameRequest gameRequest) {
        return constructionService.construct(playerId, gameRequest);
    }

    @Override
    public GameConstruction getConstruct(long session) {
        return constructionService.getConstruct(playerId, session);
    }

    @Override
    public ClientEvent getResponce(long session, long player) {
        return constructionService.getResponce(playerId, session, player);
    }

    @Override
    public GameConstruction invitationResponsed(long sessionId, InvitationResponseEvent responce) {
        return constructionService.invitationResponsed(playerId, sessionId, responce);
    }

}
