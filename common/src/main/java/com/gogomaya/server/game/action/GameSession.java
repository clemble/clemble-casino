package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.gogomaya.server.game.action.move.GameMove;

public interface GameSession<S extends GameState<?, ?>> extends Serializable {

    public long getSessionId();

    public GameTable<? extends GameSession<S>> getTable();

    public Set<Long> getPlayers();

    public GameSessionState getSessionState();

    public List<? extends GameMove> getMadeMoves();

}
