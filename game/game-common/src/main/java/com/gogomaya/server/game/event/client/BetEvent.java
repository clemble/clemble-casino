package com.gogomaya.server.game.event.client;

import com.gogomaya.server.event.AbstractClientEvent;

public class BetEvent extends AbstractClientEvent {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 4761116695040560749L;

    final private long bet;

    public BetEvent(long playerId, long bet) {
        super(playerId);
        this.bet = bet;
        if (bet < 0)
            throw new IllegalArgumentException("Bet can't be lesser than 0");

    }

    public long getBet() {
        return bet;
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
        BetEvent other = (BetEvent) obj;
        if (bet != other.bet)
            return false;
        return true;
    }

}
