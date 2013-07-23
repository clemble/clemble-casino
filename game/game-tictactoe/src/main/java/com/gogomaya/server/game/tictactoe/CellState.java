package com.gogomaya.server.game.tictactoe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.player.PlayerAware;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "cell_type")
public class CellState {

    final static public CellState DEFAULT = new CellState(PlayerAware.DEFAULT_PLAYER);

    final private long owner;

    @JsonCreator
    public CellState(@JsonProperty("owner") long owner) {
        this.owner = owner;
    }

    public long getOwner() {
        return owner;
    }

    public boolean owned() {
        return owner != PlayerAware.DEFAULT_PLAYER;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (owner ^ (owner >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CellState other = (CellState) obj;
        if (owner != other.owner)
            return false;
        return true;
    }

}
