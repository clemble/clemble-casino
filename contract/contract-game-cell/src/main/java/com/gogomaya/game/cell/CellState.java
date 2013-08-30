package com.gogomaya.game.cell;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.game.event.client.BetEvent;
import com.gogomaya.player.PlayerAware;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "cell")
@JsonTypeName("simple")
public class CellState {

    final static public CellState DEFAULT = new CellState(PlayerAware.DEFAULT_PLAYER);

    final private long owner;

    @JsonCreator
    public CellState(@JsonProperty("owner") long owner) {
        this.owner = owner;
    }

    public CellState(Collection<BetEvent> bets) {
        this(bets.toArray(new BetEvent[0]));
    }

    public CellState(BetEvent... bets) {
        this(BetEvent.whoBetMore(bets));
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
