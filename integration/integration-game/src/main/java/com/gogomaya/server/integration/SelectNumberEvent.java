package com.gogomaya.server.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.game.event.client.GameClientEvent;
import com.gogomaya.player.PlayerAware;

@JsonTypeName("selectNumber")
public class SelectNumberEvent extends GameClientEvent {

    private static final long serialVersionUID = 6819390579455234704L;

    final private int number;

    @JsonCreator
    public SelectNumberEvent(@JsonProperty(PlayerAware.JSON_ID) String player, @JsonProperty("number") int number) {
        super(player);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + number;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        SelectNumberEvent other = (SelectNumberEvent) obj;
        if (number != other.number)
            return false;
        return true;
    }

}
