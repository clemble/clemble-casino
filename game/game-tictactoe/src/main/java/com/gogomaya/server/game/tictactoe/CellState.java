package com.gogomaya.server.game.tictactoe;

import com.gogomaya.server.player.PlayerAware;

public class CellState {

    final static public CellState DEFAULT_STATE = new CellState(PlayerAware.DEFAULT_PLAYER);

    final private long owner;

    public CellState(long owner) {
        this.owner = owner;
    }

    public long getOwner() {
        return owner;
    }

    public boolean owned() {
        return owner != PlayerAware.DEFAULT_PLAYER;
    }

}
