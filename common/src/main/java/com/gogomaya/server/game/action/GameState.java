package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.Collection;

public interface GameState extends Serializable {

    public Collection<? extends GamePlayerState> getPlayersStates();

    public Collection<? extends GameMove> getNextMoves();

    public Collection<? extends GameMove> getMadeMoves();

}
