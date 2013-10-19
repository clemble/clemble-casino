package com.clemble.casino.payment;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;

@Entity
@Table(name = "PAYMENT_TRANSACTION")
public class PaymentTransaction implements Serializable {

    /**
     * Generated 05/05/13
     */
    private static final long serialVersionUID = 2610517770966910840L;

    @EmbeddedId
    private PaymentTransactionKey transactionId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PAYMENT_TRANSACTION_OPERATION", joinColumns = { @JoinColumn(name = "TRANSACTION_ID"), @JoinColumn(name = "MONEY_SOURCE") })
    private Set<PaymentOperation> paymentOperations = new HashSet<PaymentOperation>();

    @Column(name = "TRANSACTION_DATE")
    private Date transactionDate;

    public PaymentTransactionKey getTransactionKey() {
        return transactionId;
    }

    public PaymentTransaction setTransactionKey(PaymentTransactionKey transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public boolean isParticipant(String player) {
        for (PaymentOperation paymentOperation: paymentOperations)
            if (paymentOperation.getPlayer().equals(player))
                return true;
        return false;
    }

    public Set<PaymentOperation> getPaymentOperations() {
        return paymentOperations;
    }

    public PaymentTransaction setPaymentOperations(Set<PaymentOperation> paymentOperations) {
        this.paymentOperations = paymentOperations;
        return this;
    }

    public PaymentTransaction addPaymentOperation(PaymentOperation paymentOperation) {
        if (paymentOperation != null)
            this.paymentOperations.add(paymentOperation);
        return this;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public PaymentTransaction setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public boolean valid() {
        // Step 1. Checking currency
        Currency currency = null;
        for (PaymentOperation paymentOperation : paymentOperations) {
            if (currency == null) {
                currency = paymentOperation.getAmount().getCurrency();
            } else if (currency != paymentOperation.getAmount().getCurrency()) {
                return false;
            }
        }
        // Step 2. Checking credit and debit amount match up
        Money creditAmount = null;
        Money debitAmount = null;
        for (PaymentOperation paymentOperation : paymentOperations) {
            Money amount = paymentOperation.getAmount();
            if (amount.getAmount() > 0) {
                switch (paymentOperation.getOperation()) {
                case Credit:
                    creditAmount = creditAmount == null ? paymentOperation.getAmount() : creditAmount.add(amount);
                    break;
                case Debit:
                    debitAmount = debitAmount == null ? paymentOperation.getAmount() : debitAmount.add(paymentOperation.getAmount());
                    break;
                }
            } else {
                amount = amount.negate();
                switch (paymentOperation.getOperation()) {
                case Credit:
                    debitAmount = debitAmount == null ? paymentOperation.getAmount() : debitAmount.add(paymentOperation.getAmount());
                    break;
                case Debit:
                    creditAmount = creditAmount == null ? paymentOperation.getAmount() : creditAmount.add(amount);
                    break;
                }
            }
        }
        return (creditAmount != null && debitAmount != null)
                && creditAmount.getAmount() == debitAmount.getAmount()
                && creditAmount.getCurrency() == debitAmount.getCurrency();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((transactionDate == null) ? 0 : transactionDate.hashCode());
        result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
        result = prime * result + ((paymentOperations == null) ? 0 : paymentOperations.hashCode());
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
        PaymentTransaction other = (PaymentTransaction) obj;
        if (transactionDate == null) {
            if (other.transactionDate != null)
                return false;
        } else if (!transactionDate.equals(other.transactionDate))
            return false;
        if (transactionId == null) {
            if (other.transactionId != null)
                return false;
        } else if (!transactionId.equals(other.transactionId))
            return false;
        if (paymentOperations == null) {
            if (other.paymentOperations != null)
                return false;
        } else if (!paymentOperations.equals(other.paymentOperations))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PaymentTransaction [transactionId=" + transactionId + ", paymentOperations=" + paymentOperations + ", transactionDate=" + transactionDate + "]";
    }

}
