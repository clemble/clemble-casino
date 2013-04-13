package com.gogomaya.server.game.rule.bet;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.GameRuleOptions;

@Embeddable
public class FixedBetRule implements BetRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 6656576325438885626L;

    final public static FixedBetRule DEFAULT = new FixedBetRule(50);
    final public static GameRuleOptions<BetRule> DEFAULT_OPTIONS = new GameRuleOptions<BetRule>(new FixedBetRule(50), new FixedBetRule(100), new FixedBetRule(
            200));

    @Column(name = "PRICE")
    private int price;

    public FixedBetRule() {
    }

    public FixedBetRule(final int priceToUse) {
        if (priceToUse < 0)
            throw GogomayaException.create(GogomayaError.ClientJsonFormatError);
        this.price = priceToUse;
    }

    public int getPrice() {
        return price;
    }

    public FixedBetRule setPrice(int price) {
        this.price = price;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + price;
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
        if (price != other.price)
            return false;
        return true;
    }

}