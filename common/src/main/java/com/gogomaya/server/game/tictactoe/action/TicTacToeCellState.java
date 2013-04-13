package com.gogomaya.server.game.tictactoe.action;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class TicTacToeCellState {

    final public static TicTacToeCellState DEFAULT_CELL_STATE = new TicTacToeCellState(0, 0, 0);

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
        return owner != 0;
    }

    public long getFirstPlayerBet() {
        return firstPlayerBet;
    }

    public long getSecondPlayerBet() {
        return secondPlayerBet;
    }
}
