package com.gogomaya.server.game.action.tictactoe;

import com.gogomaya.server.game.action.AbstractGamePlayerState;

public class TicTacToePlayerState extends AbstractGamePlayerState {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = 250689205093466915L;

    private final long moneyTotal;

    private final long moneyOnTable;

    public TicTacToePlayerState(final long playerId, final long money, final long moneyOnTable) {
        super(playerId);
        this.moneyTotal = money;
        this.moneyOnTable = moneyOnTable;
    }

    public long getMoneyTotal() {
        return moneyTotal;
    }

    public long getMoneyOnTable() {
        return moneyOnTable;
    }

}
