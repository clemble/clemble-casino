package com.clemble.casino.server.event.payment;

import com.clemble.casino.payment.PendingTransaction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 16/10/14.
 */
public class SystemPaymentFreezeRequestEvent implements SystemPaymentEvent {

    final public static String CHANNEL = "sys:payment:freeze:request";

    final private PendingTransaction transaction;

    @JsonCreator
    public SystemPaymentFreezeRequestEvent(
        @JsonProperty("transaction") PendingTransaction transaction
    ) {
        this.transaction = transaction;
    }

    public PendingTransaction getTransaction() {
        return transaction;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemPaymentFreezeRequestEvent that = (SystemPaymentFreezeRequestEvent) o;

        if (transaction != null ? !transaction.equals(that.transaction) : that.transaction != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return transaction != null ? transaction.hashCode() : 0;
    }

    @Override
    public String toString() {
        return transaction.getTransactionKey() + " > " + CHANNEL;
    }
}

