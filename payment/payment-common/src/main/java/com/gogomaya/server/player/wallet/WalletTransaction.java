package com.gogomaya.server.player.wallet;

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

import org.hibernate.annotations.TypeDef;

import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneyHibernate;

@Entity
@Table(name = "PLAYER_WALLET_TRANSACTION")
@TypeDef(name = "money", typeClass = MoneyHibernate.class)
public class WalletTransaction implements Serializable {

    /**
     * Generated 05/05/13
     */
    private static final long serialVersionUID = 2610517770966910840L;

    @EmbeddedId
    private WalletTransactionId transactionId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PLAYER_WALLET_OPERATION", joinColumns = { @JoinColumn(name = "TRANSACTION_ID"), @JoinColumn(name = "MONEY_SOURCE") })
    private Set<WalletOperation> walletOperations = new HashSet<WalletOperation>();

    @Column(name = "TRANSACTION_DATE")
    private Date transactionDate;

    public WalletTransactionId getTransactionId() {
        return transactionId;
    }

    public WalletTransaction setTransactionId(WalletTransactionId transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public Set<WalletOperation> getWalletOperations() {
        return walletOperations;
    }

    public WalletTransaction setWalletOperations(Set<WalletOperation> walletOperations) {
        this.walletOperations = walletOperations;
        return this;
    }

    public WalletTransaction addWalletOperation(WalletOperation walletOperation) {
        if (walletOperation != null)
            this.walletOperations.add(walletOperation);
        return this;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public WalletTransaction setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public boolean valid() {
        // Step 1. Checking currency
        Currency currency = null;
        for (WalletOperation walletOperation : walletOperations) {
            if (currency == null) {
                currency = walletOperation.getAmmount().getCurrency();
            } else if (currency != walletOperation.getAmmount().getCurrency()) {
                return false;
            }
        }
        // Step 2. Checking credit and debit ammount match up
        Money creditAmmount = null;
        Money debitAmmount = null;
        for (WalletOperation walletOperation : walletOperations) {
            Money ammount = walletOperation.getAmmount();
            if (ammount.getAmount() > 0) {
                switch (walletOperation.getOperation()) {
                case Credit:
                    creditAmmount = creditAmmount == null ? walletOperation.getAmmount() : creditAmmount.add(ammount);
                    break;
                case Debit:
                    debitAmmount = debitAmmount == null ? walletOperation.getAmmount() : debitAmmount.add(walletOperation.getAmmount());
                    break;
                }
            } else {
                ammount = ammount.negate();
                switch (walletOperation.getOperation()) {
                case Credit:
                    debitAmmount = debitAmmount == null ? walletOperation.getAmmount() : debitAmmount.add(walletOperation.getAmmount());
                    break;
                case Debit:
                    creditAmmount = creditAmmount == null ? walletOperation.getAmmount() : creditAmmount.add(ammount);
                    break;
                }
            }
        }
        return (creditAmmount != null && debitAmmount != null) && creditAmmount.getAmount() == debitAmmount.getAmount()
                && creditAmmount.getCurrency() == debitAmmount.getCurrency();
    }

}
