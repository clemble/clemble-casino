package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.Set;

public interface GameSession<S extends GameState<?, ?>> extends Serializable {

    public long getSessionId();

    public GameSessionState getSessionState();

    public GameTable<? extends GameSession<S>> getTable();

    public S getGameState();

    public Set<Long> getPlayers();

}
