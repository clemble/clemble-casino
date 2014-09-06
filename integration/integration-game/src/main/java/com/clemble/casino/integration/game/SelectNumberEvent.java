package com.clemble.casino.integration.game;

import com.clemble.casino.game.action.ClientGameEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("selectNumber")
public class SelectNumberEvent implements ClientGameEvent {

    private static final long serialVersionUID = 6819390579455234704L;

    final private String player;
    final private int number;

    @JsonCreator
    public SelectNumberEvent(@JsonProperty(PLAYER) String player, @JsonProperty("number") int number) {
        this.player = player;
        this.number = number;
    }

    @Override
    public String getPlayer(){
        return player;
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
        if (getClass() != obj.getClass())
            return false;
        SelectNumberEvent other = (SelectNumberEvent) obj;
        if (number != other.number)
            return false;
        return true;
    }

}
