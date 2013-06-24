package com.gogomaya.server.game.tictactoe.event.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.game.event.client.BetEvent;

@JsonTypeName("bet")
public class TicTacToeBetOnCellEvent extends BetEvent implements TicTacToeEvent {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = 8683356678866667739L;

    public TicTacToeBetOnCellEvent(final long playerId) {
        this(playerId, 0);
    }

    @JsonCreator
    public TicTacToeBetOnCellEvent(@JsonProperty("playerId") final long playerId, @JsonProperty("bet") final long bet) {
        super(playerId, bet);
    }

}
