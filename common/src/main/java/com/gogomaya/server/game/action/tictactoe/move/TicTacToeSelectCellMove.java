package com.gogomaya.server.game.action.tictactoe.move;

public class TicTacToeSelectCellMove extends TicTacToeMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3938747678529156779L;

    final private byte row;

    final private byte column;

    public TicTacToeSelectCellMove(final long playerId, final byte row, final byte column) {
        super(playerId, TicTacToeMoveType.SelectCell);

        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}
