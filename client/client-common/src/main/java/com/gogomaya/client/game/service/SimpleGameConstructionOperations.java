package com.gogomaya.client.game.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.event.listener.ConstructionEventSelector;
import com.gogomaya.event.listener.EventListener;
import com.gogomaya.event.listener.EventListenersManager;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.game.event.schedule.InvitationDeclinedEvent;
import com.gogomaya.game.event.schedule.InvitationResponseEvent;
import com.gogomaya.game.service.GameConstructionService;

public class SimpleGameConstructionOperations implements GameConstructionOperations {

    final private long playerId;
    final private EventListenersManager listenersManager;
    final private GameConstructionService constructionService;

    public SimpleGameConstructionOperations(long playerId, GameConstructionService constructionService, EventListenersManager listenersManager) {
        this.playerId = playerId;
        this.constructionService = checkNotNull(constructionService);
        this.listenersManager = checkNotNull(listenersManager);
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
    public GameConstruction accept(long sessionId) {
        return response(sessionId, new InvitationAcceptedEvent(sessionId, playerId));
    }

    @Override
    public GameConstruction decline(long sessionId) {
        return response(sessionId, new InvitationDeclinedEvent(sessionId, playerId));
    }

    @Override
    public GameConstruction response(long sessionId, InvitationResponseEvent responce) {
        return constructionService.reply(playerId, sessionId, responce);
    }

    @Override
    public ClientEvent getResponce(long session, long player) {
        return constructionService.getResponce(playerId, session, player);
    }

    @Override
    public void subscribe(long sessionId, EventListener constructionListener) {
        listenersManager.subscribe(new ConstructionEventSelector(sessionId), constructionListener);
    }

    @Override
    public String getGameActionServer(long sessionId) {
        return constructionService.getServer(playerId, sessionId);
    }

}
