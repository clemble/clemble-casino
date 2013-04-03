package com.gogomaya.server.game.action.tictactoe.move;

import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class TicTacToeSelectCellMove extends TicTacToeMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3938747678529156779L;

    final private Entry<Byte, Byte> cell;

    public TicTacToeSelectCellMove(final long playerId, final byte row, final byte column) {
        super(playerId, TicTacToeMoveType.SelectCell);

        this.cell = new ImmutablePair<Byte, Byte>(row, column);
    }

    public Entry<Byte, Byte> getCell() {
        return cell;
    }

}
