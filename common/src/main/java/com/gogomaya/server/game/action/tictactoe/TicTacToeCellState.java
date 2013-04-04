package com.gogomaya.server.game.action.tictactoe;

public class TicTacToeCellState {

    final public static TicTacToeCellState DEFAULT_CELL_STATE = new TicTacToeCellState(0, 0, 0);

    final private long owner;

    final private long firstPlayerBet;

    final private long secondPlayerBet;

    public TicTacToeCellState(long owner, long firstPlayerBet, long secondPlayerBet) {
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
