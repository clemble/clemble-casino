package com.gogomaya.server.game.action.tictactoe.move;

public class TicTacToeBetOnCellMove extends TicTacToeMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = 8683356678866667739L;

    final private long bet;

    public TicTacToeBetOnCellMove(final long playerId, final long bet) {
        super(playerId, TicTacToeMoveType.BetOnCell);
        this.bet = bet;
    }

    public long getBet() {
        return bet;
    }

}
