package com.clemble.casino.client.game.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.event.listener.ConstructionEventSelector;
import com.clemble.casino.event.listener.EventListener;
import com.clemble.casino.event.listener.EventListenersManager;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameRequest;
import com.clemble.casino.game.event.schedule.InvitationAcceptedEvent;
import com.clemble.casino.game.event.schedule.InvitationDeclinedEvent;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.service.GameConstructionService;

public class SimpleGameConstructionOperations<T extends GameState> implements GameConstructionOperations<T> {

    final private String player;
    final private Game game;
    final private GameActionOperations<T> actionOperations;
    final private EventListenersManager listenersManager;
    final private GameConstructionService constructionService;

    public SimpleGameConstructionOperations(String player, Game game, GameActionOperations<T> serverRegistry, GameConstructionService constructionService, EventListenersManager listenersManager) {
        this.player = player;
        this.game = game;
        this.actionOperations = serverRegistry;
        this.constructionService = checkNotNull(constructionService);
        this.listenersManager = checkNotNull(listenersManager);
    }

    @Override
    public GameConstruction construct(GameRequest gameRequest) {
        return constructionService.construct(player, gameRequest);
    }

    @Override
    public GameConstruction getConstruct(String session) {
        return constructionService.getConstruct(player, session);
    }

    @Override
    public GameConstruction accept(String sessionId) {
        return response(sessionId, new InvitationAcceptedEvent(new GameSessionKey(game, sessionId), player));
    }

    @Override
    public GameConstruction decline(String sessionId) {
        return response(sessionId, new InvitationDeclinedEvent(player, new GameSessionKey(game, sessionId)));
    }

    @Override
    public GameConstruction response(String sessionId, InvitationResponseEvent responce) {
        return constructionService.reply(player, sessionId, responce);
    }

    @Override
    public ClientEvent getResponce(String session, String player) {
        return constructionService.getResponce(player, session, player);
    }

    @Override
    public void subscribe(String session, EventListener constructionListener) {
        listenersManager.subscribe(new ConstructionEventSelector(new GameSessionKey(game, session)), constructionListener);
    }

    @Override
    public GameActionOperations<T> getActionOperations(GameSessionKey gameSessionKey) {
        return actionOperations;
    }

}
