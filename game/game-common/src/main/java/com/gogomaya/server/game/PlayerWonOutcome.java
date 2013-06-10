package com.gogomaya.server.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
}
