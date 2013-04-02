package com.gogomaya.server.game.action.tictactoe;

public class TicTacToeCellState {

    final private int state;

    final private int firstPlayerBet;

    final private int secondPlayerBet;

    public TicTacToeCellState(int state, int firstPlayerBet, int secondPlayerBet) {
        this.state = state;
        this.firstPlayerBet = firstPlayerBet;
        this.secondPlayerBet = secondPlayerBet;
    }

    public int getState() {
        return state;
    }

    public int getFirstPlayerBet() {
        return firstPlayerBet;
    }

    public int getSecondPlayerBet() {
        return secondPlayerBet;
    }
}
