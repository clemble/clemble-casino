package com.gogomaya.server.payment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.gogomaya.server.money.MoneySource;

@Embeddable
public class PaymentTransactionId implements Serializable {

    /**
     * Generated 05/05/13
     */
    private static final long serialVersionUID = 1305768471476355952L;

    @Column(name = "TRANSACTION_ID")
    private long transactionId;

    @Column(name = "MONEY_SOURCE")
    @Enumerated(EnumType.STRING)
    private MoneySource source;

    public PaymentTransactionId() {
    }

    public PaymentTransactionId(String source, long transactionId) {
        this(MoneySource.valueOf(source), transactionId);
    }

    public PaymentTransactionId(MoneySource source, long transactionId) {
        this.source = source;
        this.transactionId = transactionId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public PaymentTransactionId setTransactionId(long transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public MoneySource getSource() {
        return source;
    }

    public PaymentTransactionId setSource(MoneySource source) {
        this.source = source;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        result = prime * result + (int) (transactionId ^ (transactionId >>> 32));
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
        PaymentTransactionId other = (PaymentTransactionId) obj;
        if (source != other.source)
            return false;
        if (transactionId != other.transactionId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PaymentTransactionId [transactionId=" + transactionId + ", source=" + source + "]";
    }

}
