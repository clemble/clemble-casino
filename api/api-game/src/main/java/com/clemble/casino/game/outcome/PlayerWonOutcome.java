package com.clemble.casino.game.outcome;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("playerWon")
public class PlayerWonOutcome extends GameOutcome {

    /**
     * 
     */
    private static final long serialVersionUID = -3259192146118069428L;

    final private String winner;

    @JsonCreator
    public PlayerWonOutcome(@JsonProperty("winner") String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    @Override
    public int hashCode() {
        return winner == null ? 0 : winner.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerWonOutcome other = (PlayerWonOutcome) obj;
        return winner.equals(other.winner);
    }
}
