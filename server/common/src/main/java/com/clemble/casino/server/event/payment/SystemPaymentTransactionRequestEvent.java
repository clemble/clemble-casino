package com.clemble.casino.server.event.payment;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.server.event.SystemEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 7/5/14.
 */
public class SystemPaymentTransactionRequestEvent implements SystemEvent {

    final public static String CHANNEL = "payment:transaction:request";

    final private PaymentTransaction transaction;

    @JsonCreator
    public SystemPaymentTransactionRequestEvent(@JsonProperty("transaction") PaymentTransaction transaction) {
        this.transaction = transaction;
    }

    public PaymentTransaction getTransaction() {
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

        SystemPaymentTransactionRequestEvent that = (SystemPaymentTransactionRequestEvent) o;

        if (transaction != null ? !transaction.equals(that.transaction) : that.transaction != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return transaction != null ? transaction.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "sys:" + transaction.getTransactionKey().toString() + ":" + CHANNEL;
    }
}
