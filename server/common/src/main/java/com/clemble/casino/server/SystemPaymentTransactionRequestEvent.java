package com.clemble.casino.server;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.server.event.SystemEvent;

/**
 * Created by mavarazy on 7/5/14.
 */
public class SystemPaymentTransactionRequestEvent implements SystemEvent {

    final public static String CHANNEL = "payment:transaction:request";

    final private PaymentTransaction transaction;

    public SystemPaymentTransactionRequestEvent(PaymentTransaction transaction) {
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
}
