package com.gogomaya.server.game.tictactoe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TicTacToeCellState {

    final public static long DEFAULT_OWNER = -1L;
    final public static TicTacToeCellState DEFAULT_CELL_STATE = new TicTacToeCellState(DEFAULT_OWNER, -1, -1);

    @JsonProperty("owner")
    final private long owner;

    @JsonProperty("firstPlayerBet")
    final private long firstPlayerBet;

    @JsonProperty("secondPlayerBet")
    final private long secondPlayerBet;

    @JsonCreator
    public TicTacToeCellState(@JsonProperty("owner") long owner, @JsonProperty("firstPlayerBet") long firstPlayerBet, @JsonProperty("secondPlayerBet") long secondPlayerBet) {
        this.owner = owner;
        this.firstPlayerBet = firstPlayerBet;
        this.secondPlayerBet = secondPlayerBet;
    }

    public long getOwner() {
        return owner;
    }

    public boolean owned() {
        return owner != DEFAULT_OWNER;
    }

    public long getFirstPlayerBet() {
        return firstPlayerBet;
    }

    public long getSecondPlayerBet() {
        return secondPlayerBet;
    }

    @Override
    public String toString() {
        return "CellState [owner=" + owner + ", first=" + firstPlayerBet + ", second=" + secondPlayerBet + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (firstPlayerBet ^ (firstPlayerBet >>> 32));
        result = prime * result + (int) (owner ^ (owner >>> 32));
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
        TicTacToeCellState other = (TicTacToeCellState) obj;
        if (firstPlayerBet != other.firstPlayerBet)
            return false;
        if (owner != other.owner)
            return false;
        if (secondPlayerBet != other.secondPlayerBet)
            return false;
        return true;
    }
}
