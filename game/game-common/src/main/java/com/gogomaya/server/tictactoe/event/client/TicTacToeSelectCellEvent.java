package com.gogomaya.server.tictactoe.event.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.game.event.client.GameClientEvent;
import com.gogomaya.server.tictactoe.TicTacToeCell;

@JsonIgnoreProperties(value = { "row", "column" })
public class TicTacToeSelectCellEvent extends GameClientEvent implements TicTacToeEvent {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3938747678529156779L;

    final private TicTacToeCell cell;

    public TicTacToeSelectCellEvent(final long playerId) {
        this(playerId, Byte.MIN_VALUE, Byte.MIN_VALUE);
    }

    public TicTacToeSelectCellEvent(final long playerId, final byte row, final byte column) {
        this(playerId, TicTacToeCell.create(row, column));
    }

    @JsonCreator
    public TicTacToeSelectCellEvent(@JsonProperty("playerId") final long playerId, @JsonProperty("cell") final TicTacToeCell cell) {
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
        TicTacToeSelectCellEvent other = (TicTacToeSelectCellEvent) obj;
        if (cell == null) {
            if (other.cell != null)
                return false;
        } else if (!cell.equals(other.cell))
            return false;
        return true;
    }

}
