package com.gogomaya.server.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("playerWon")
public class PlayerWonOutcome extends GameOutcome {

    /**
     * 
     */
    private static final long serialVersionUID = -3259192146118069428L;

    final private long winner;

    @JsonCreator
    public PlayerWonOutcome(@JsonProperty("winner") long winner) {
        this.winner = winner;
    }

    public long getWinner() {
        return winner;
    }

    @Override
    public int hashCode() {
        return 31 + (int) (winner ^ (winner >>> 32));
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
        if (winner != other.winner)
            return false;
        return true;
    }
}
