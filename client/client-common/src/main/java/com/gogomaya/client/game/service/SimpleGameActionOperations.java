package com.gogomaya.client.game.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.event.listener.EventListener;
import com.gogomaya.event.listener.EventListenersManager;
import com.gogomaya.event.listener.SessionEventSelector;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.client.MadeMove;
import com.gogomaya.game.service.GameActionService;

public class SimpleGameActionOperations<State extends GameState> implements GameActionOperations<State> {

    /**
     * 
     */
    private static final long serialVersionUID = -2263303118851762598L;

    final private long playerId;
    final private long sessionId;
    final private GameActionService<State> gameActionService;
    final private EventListenersManager eventListenersManager;

    public SimpleGameActionOperations(long playerId, long sessionId, EventListenersManager eventListenersManager, GameActionService<State> gameActionService) {
        this.playerId = playerId;
        this.sessionId = sessionId;
        this.eventListenersManager = checkNotNull(eventListenersManager);
        this.gameActionService = checkNotNull(gameActionService);
    }

    @Override
    public State process(ClientEvent move) {
        return gameActionService.process(sessionId, move);
    }

    @Override
    public MadeMove getAction(int actionId) {
        return gameActionService.getAction(sessionId, actionId);
    }

    @Override
    public void subscribe(EventListener eventListener) {
        eventListenersManager.subscribe(new SessionEventSelector(sessionId), eventListener);
    }

    @Override
    public long getSession() {
        return sessionId;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public GameActionOperations<State> clone(String server) {
        // TODO Auto-generated method stub
        return null;
    }

}
