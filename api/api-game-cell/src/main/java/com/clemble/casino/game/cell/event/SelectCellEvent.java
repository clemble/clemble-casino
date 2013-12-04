package com.clemble.casino.game.cell.event;

import com.clemble.casino.game.cell.Cell;
import com.clemble.casino.game.event.client.GameClientEvent;
import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("select")
public class SelectCellEvent extends GameClientEvent {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3938747678529156779L;

    final private Cell cell;

    @JsonCreator
    public SelectCellEvent(@JsonProperty(PlayerAware.JSON_ID) final String player, @JsonProperty("cell") final Cell cell) {
        super(player);
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    @Override
    public String toString() {
        return "{cell:" + cell + ", player: " + getPlayer() + "}";
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
