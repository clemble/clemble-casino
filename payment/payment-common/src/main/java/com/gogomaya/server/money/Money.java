package com.gogomaya.server.money;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

public class Money implements Serializable {

    /**
     * Generated 07/04/13
     */
    private static final long serialVersionUID = -2196796622087364501L;

    private final Currency currency;

    private final long amount;

    public Money(final Currency currency, final long amount) {
        this.currency = checkNotNull(currency);
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money add(Money more) {
        if (more == null || more.amount == 0)
            return this;

        more = more.exchange(more, currency);

        return Money.create(currency, amount + more.amount);
    }

    public Money subtract(Money minus) {
        if (minus == null || minus.amount == 0)
            return this;

        minus = exchange(minus, currency);

        return Money.create(currency, amount - minus.amount);
    }

    public Money negate() {
        if (amount == 0)
            return this;

        return Money.create(currency, -amount);
    }

    public Money exchange(Money source, Currency targetCurrency) {
        if (source == null || source.getCurrency() == targetCurrency)
            return source;

        throw new IllegalArgumentException("We do not support money change yet");
    }

    public static Money create(final Currency currency, final long ammount) {
        return new Money(currency, ammount);
    }

    @Override
    public String toString() {
        return "Money [currency=" + currency + ", amount=" + amount + "]";
    }

}
