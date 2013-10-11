package com.clemble.casino.game.cell;

import java.util.Collection;

import com.clemble.casino.game.event.client.BetEvent;
import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "cell")
@JsonTypeName("simple")
public class CellState {

    final static public CellState DEFAULT = new CellState(PlayerAware.DEFAULT_PLAYER);

    final private String owner;

    @JsonCreator
    public CellState(@JsonProperty("owner") String owner) {
        this.owner = owner;
    }

    public CellState(Collection<BetEvent> bets) {
        this(bets.toArray(new BetEvent[0]));
    }

    public CellState(BetEvent... bets) {
        this(BetEvent.whoBetMore(bets));
    }

    public String getOwner() {
        return owner;
    }

    public boolean owned() {
        return owner != PlayerAware.DEFAULT_PLAYER;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (owner == null ? 0 : owner.hashCode());
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
