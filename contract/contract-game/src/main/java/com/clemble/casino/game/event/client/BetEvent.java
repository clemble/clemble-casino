package com.clemble.casino.game.event.client;

import java.util.Arrays;
import java.util.Collection;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("bet")
public class BetEvent extends GameClientEvent {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 4761116695040560749L;

    final private long bet;

    @JsonCreator
    public BetEvent(@JsonProperty(PlayerAware.JSON_ID) String player, @JsonProperty("bet") long bet) {
        super(player);
        this.bet = bet;
        if (bet < 0)
            throw new IllegalArgumentException("Bet can't be lesser than 0");

    }

    public long getBet() {
        return bet;
    }

    static public String whoBetMore(BetEvent[] bets) {
        return whoBetMore(Arrays.asList(bets));
    }

    static public String whoBetMore(Collection<BetEvent> bets) {
        if (bets == null || bets.size() == 0)
            return PlayerAware.DEFAULT_PLAYER;

        long maxBet = 0;
        String playerWithMaxBet = null;
        for (BetEvent bet : bets) {
            if (bet.getBet() > maxBet) {
                maxBet = bet.getBet();
                playerWithMaxBet = bet.getPlayer();
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
