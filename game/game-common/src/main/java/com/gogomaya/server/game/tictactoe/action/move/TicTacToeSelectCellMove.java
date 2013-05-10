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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cell == null) ? 0 : cell.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TicTacToeSelectCellMove other = (TicTacToeSelectCellMove) obj;
        if (cell == null) {
            if (other.cell != null)
                return false;
        } else if (!cell.equals(other.cell))
            return false;
        return true;
    }

}
