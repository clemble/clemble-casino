package com.gogomaya.server.game.action;

public interface GameStateCache<State extends GameState> {

    public State getStateForSession(long sessionId);

    public void updateState(long sessionId, State newState);

    public void cleanState(long sessionId);

}
