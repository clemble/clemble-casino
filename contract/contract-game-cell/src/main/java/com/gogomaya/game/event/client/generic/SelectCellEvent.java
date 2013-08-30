package com.gogomaya.game.event.client.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.game.cell.Cell;
import com.gogomaya.game.event.client.GameClientEvent;

@JsonTypeName("select")
public class SelectCellEvent extends GameClientEvent {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3938747678529156779L;

    final private Cell cell;

    @JsonCreator
    public SelectCellEvent(@JsonProperty("playerId") final long playerId, @JsonProperty("cell") final Cell cell) {
        super(playerId);
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    @Override
    public String toString() {
        return "{cell:" + cell + ", player: " + getPlayerId() + "}";
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
        SelectCellEvent other = (SelectCellEvent) obj;
        if (cell == null) {
            if (other.cell != null)
                return false;
        } else if (!cell.equals(other.cell))
            return false;
        return true;
    }

}
