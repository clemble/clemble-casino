package com.clemble.casino.integration.game;

import com.clemble.casino.game.lifecycle.management.event.action.PlayerGameAction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(SelectNumberAction.JSON_TYPE)
public class SelectNumberAction implements PlayerGameAction {

    final public static String JSON_TYPE = "game:action:select:number";

    private static final long serialVersionUID = 6819390579455234704L;

    final private int number;

    @JsonCreator
    public SelectNumberAction(@JsonProperty("number") int number) {
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
        if (getClass() != obj.getClass())
            return false;
        SelectNumberAction other = (SelectNumberAction) obj;
        if (number != other.number)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JSON_TYPE + " > " + number;
    }


}
