package com.gogomaya.server.game.tictactoe.action.move;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.tictactoe.action.TicTacToeCell;

@JsonIgnoreProperties(value = { "row", "column" })
public class TicTacToeSelectCellMove extends TicTacToeMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3938747678529156779L;

    final private TicTacToeCell cell;

    public TicTacToeSelectCellMove(final long playerId) {
        this(playerId, Byte.MIN_VALUE, Byte.MIN_VALUE);
    }

    public TicTacToeSelectCellMove(final long playerId, final byte row, final byte column) {
        this(playerId, TicTacToeCell.create(row, column));
    }

    @JsonCreator
    public TicTacToeSelectCellMove(@JsonProperty("playerId") final long playerId, @JsonProperty("cell") final TicTacToeCell cell) {
        super(playerId);
        this.cell = cell;
    }

    @JsonIgnore
    public byte getRow() {
        return cell.getRow();
    }

    @JsonIgnore
    public byte getColumn() {
        return cell.getColumn();
    }

    public TicTacToeCell getCell() {
        return cell;
    }

    @Override
    public String toString() {
        return "SelectCell [cell=" + cell + ", player = " + getPlayerId() + "]";
    }

}
