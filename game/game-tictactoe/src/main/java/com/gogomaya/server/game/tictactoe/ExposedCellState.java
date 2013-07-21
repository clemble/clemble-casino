package com.gogomaya.server.game.tictactoe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.player.PlayerAware;

public class ExposedCellState extends CellState {

    final public static ExposedCellState DEFAULT_CELL_STATE = new ExposedCellState(PlayerAware.DEFAULT_PLAYER, 0, 0);

    @JsonProperty("firstPlayerBet")
    final private long firstPlayerBet;

    @JsonProperty("secondPlayerBet")
    final private long secondPlayerBet;

    @JsonCreator
    public ExposedCellState(@JsonProperty("owner") long owner,
            @JsonProperty("firstPlayerBet") long firstPlayerBet,
            @JsonProperty("secondPlayerBet") long secondPlayerBet) {
        super(owner);
        this.firstPlayerBet = firstPlayerBet;
        this.secondPlayerBet = secondPlayerBet;
    }

    public long getFirstPlayerBet() {
        return firstPlayerBet;
    }

    public long getSecondPlayerBet() {
        return secondPlayerBet;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (firstPlayerBet ^ (firstPlayerBet >>> 32));
        result = prime * result + (int) (secondPlayerBet ^ (secondPlayerBet >>> 32));
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
        if (firstPlayerBet != other.firstPlayerBet)
            return false;
        if (secondPlayerBet != other.secondPlayerBet)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ExposedCellState [firstPlayerBet=" + firstPlayerBet + ", secondPlayerBet=" + secondPlayerBet + "]";
    }

}