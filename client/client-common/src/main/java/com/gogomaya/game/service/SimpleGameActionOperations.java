package com.gogomaya.game.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.client.MadeMove;

public class SimpleGameActionOperations<State extends GameState> implements GameActionOperations<State> {

    /**
     * 
     */
    private static final long serialVersionUID = -2263303118851762598L;

    final private long playerId;
    final private long sessionId;
    final private GameActionService<State> gameActionService;

    public SimpleGameActionOperations(long playerId, long sessionId, GameActionService<State> gameActionService) {
        this.playerId = playerId;
        this.sessionId = sessionId;
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
    public long getSession() {
        return sessionId;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

}
