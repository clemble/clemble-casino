package com.clemble.casino.game.cell;

import java.util.Collection;

import com.clemble.casino.game.event.client.BetEvent;
import com.clemble.casino.utils.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("exposed")
public class ExposedCellState extends CellState {

    final private Collection<BetEvent> bets;

    @JsonCreator
    public ExposedCellState(@JsonProperty("owner") String owner, @JsonProperty("bets") Collection<BetEvent> bets) {
        super(owner);
        this.bets = CollectionUtils.<BetEvent>immutableList(bets);
    }

    public ExposedCellState(Collection<BetEvent> bets) {
        this(bets.toArray(new BetEvent[0]));
    }

    public ExposedCellState(BetEvent... bets) {
        super(BetEvent.whoBetMore(bets));
        this.bets = CollectionUtils.<BetEvent>immutableList(bets);
    }

    public Collection<BetEvent> getBets() {
        return bets;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bets == null) ? 0 : bets.hashCode());
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
        ExposedCellState other = (ExposedCellState) obj;
        if (bets == null) {
            if (other.bets != null)
                return false;
        } else if (!bets.equals(other.bets))
            return false;
        return true;
    }

}