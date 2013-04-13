package com.gogomaya.server.game.tictactoe.action.move;

import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class TicTacToeSelectCellMove extends TicTacToeMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3938747678529156779L;

    final private Entry<Byte, Byte> cell;

    public TicTacToeSelectCellMove(final long playerId) {
        this(playerId, Byte.MIN_VALUE, Byte.MIN_VALUE);
    }

    public TicTacToeSelectCellMove(final long playerId, final byte row, final byte column) {
        super(playerId, TicTacToeMoveType.SelectCell);
        if (row < 0 || row > 2)
            throw new IllegalArgumentException("Row out of 0 .. 2 range " + row);
        if (column < 0 || column > 2)
            throw new IllegalArgumentException("Column out of 0 .. 2 range " + row);

        this.cell = new ImmutablePair<Byte, Byte>(row, column);
    }

    public Entry<Byte, Byte> getCell() {
        return cell;
    }

}
