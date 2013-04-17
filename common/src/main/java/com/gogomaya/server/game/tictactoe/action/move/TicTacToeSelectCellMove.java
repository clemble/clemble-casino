package com.gogomaya.server.game.tictactoe.action.move;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.tictactoe.action.TicTacToeCell;

public class TicTacToeSelectCellMove extends TicTacToeMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3938747678529156779L;

    final private TicTacToeCell cell;

    public TicTacToeSelectCellMove(final long playerId) {
        this(playerId, Byte.MIN_VALUE, Byte.MIN_VALUE);
    }

    @JsonCreator
    public TicTacToeSelectCellMove(
            @JsonProperty("playerId") final long playerId,
            @JsonProperty("row") final byte row,
            @JsonProperty("column") final byte column) {
        super(playerId);
        this.cell = new TicTacToeCell(row, column);
    }

    public byte getRow() {
        return cell.getRow();
    }

    public byte getColumn() {
        return cell.getColumn();
    }

    public TicTacToeCell getCell() {
        return cell;
    }

}
