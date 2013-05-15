package com.gogomaya.server.game.tictactoe.action.move;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TicTacToeBetOnCellMove extends TicTacToeMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = 8683356678866667739L;

    final private long bet;

    public TicTacToeBetOnCellMove(final long playerId) {
        this(playerId, 0);
    }

    @JsonCreator
    public TicTacToeBetOnCellMove(@JsonProperty("playerId") final long playerId, @JsonProperty("bet") final long bet) {
        super(playerId);
        if (bet < 0)
            throw new IllegalArgumentException("Bet can't be lesser than 0");

        this.bet = bet;
    }

    public long getBet() {
        return bet;
    }

    @Override
    public String toString() {
        return "BetOnCell [bet=" + bet + ", player = " + getPlayerId() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (bet ^ (bet >>> 32));
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
        TicTacToeBetOnCellMove other = (TicTacToeBetOnCellMove) obj;
        if (bet != other.bet)
            return false;
        return true;
    }

}
