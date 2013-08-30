package com.gogomaya.game.event.client;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.player.PlayerAware;

@JsonTypeName("bet")
public class BetEvent extends GameClientEvent {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 4761116695040560749L;

    final private long bet;

    @JsonCreator
    public BetEvent(@JsonProperty("playerId") long playerId, @JsonProperty("bet") long bet) {
        super(playerId);
        this.bet = bet;
        if (bet < 0)
            throw new IllegalArgumentException("Bet can't be lesser than 0");

    }

    public long getBet() {
        return bet;
    }

    static public long whoBetMore(BetEvent[] bets) {
        if (bets == null || bets.length == 0)
            return PlayerAware.DEFAULT_PLAYER;

        long maxBet = 0;
        long playerWithMaxBet = 0;
        for (BetEvent bet : bets) {
            if (bet.getBet() > maxBet) {
                maxBet = bet.getBet();
                playerWithMaxBet = bet.getPlayerId();
            }
        }

        return playerWithMaxBet;
    }

    static public long whoBetMore(Collection<BetEvent> bets) {
        if (bets == null || bets.size() == 0)
            return PlayerAware.DEFAULT_PLAYER;

        long maxBet = 0;
        long playerWithMaxBet = 0;
        for (BetEvent bet : bets) {
            if (bet.getBet() > maxBet) {
                maxBet = bet.getBet();
                playerWithMaxBet = bet.getPlayerId();
            }
        }

        return playerWithMaxBet;
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
