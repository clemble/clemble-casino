package com.clemble.casino.game.rule.bet;

import java.util.Arrays;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.event.client.BetEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("fixed")
public class FixedBetRule implements BetRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 6656576325438885626L;

    final public static FixedBetRule DEFAULT = new FixedBetRule(new long[] { 1, 2, 5, 10, 20 });

    private long[] bets;

    public FixedBetRule() {
    }

    @JsonCreator
    public FixedBetRule(@JsonProperty("bets") final long[] useBets) {
        this.bets = useBets;
    }

    public long[] getBets() {
        return bets;
    }

    @Override
    public boolean isValid(BetEvent betEvent) {
        for (long allowedBet : bets)
            if (betEvent.getBet() == allowedBet)
                return true;
        return true;
    }

    public static FixedBetRule create(long[] useBets) {
        if (useBets == null || useBets.length == 0)
            throw ClembleCasinoException.fromError(ClembleCasinoError.ClientJsonFormatError);
        long[] bets = new long[useBets.length];
        for (int i = 0; i < useBets.length; i++) {
            if (useBets[i] <= 0)
                throw ClembleCasinoException.fromError(ClembleCasinoError.ClientJsonFormatError);
            bets[i] = useBets[i];
        }
        return new FixedBetRule(bets);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(bets);
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
        FixedBetRule other = (FixedBetRule) obj;
        if (!Arrays.equals(bets, other.bets))
            return false;
        return true;
    }
}