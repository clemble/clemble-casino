package com.gogomaya.server.money;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;



public class Money implements Serializable {

    /**
     * Generated 07/04/13
     */
    private static final long serialVersionUID = -2196796622087364501L;

    private final Currency currency;

    private final int amount;

    public Money(final Currency currency, final int amount) {
        this.currency = checkNotNull(currency);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

}
