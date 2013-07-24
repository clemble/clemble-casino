package com.gogomaya.server.game.rule.bet;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;

@JsonTypeName("fixed")
public class FixedBetRule implements BetRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 6656576325438885626L;

    final public static FixedBetRule DEFAULT = new FixedBetRule(50);

    private int bet;

    public FixedBetRule() {
    }

    public FixedBetRule(final int betToUse) {
        if (betToUse < 0)
            throw GogomayaException.fromError(GogomayaError.ClientJsonFormatError);
        this.bet = betToUse;
    }

    public int getBet() {
        return bet;
    }

    public FixedBetRule setBet(int price) {
        this.bet = price;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bet;
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
        if (bet != other.bet)
            return false;
        return true;
    }

}