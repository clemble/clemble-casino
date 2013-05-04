package com.gogomaya.server.player.wallet;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.gogomaya.server.money.MoneySource;

@Embeddable
public class WalletTransactionId implements Serializable {

    /**
     * Generated 05/05/13
     */
    private static final long serialVersionUID = 1305768471476355952L;

    @Column(name = "TRANSACTION_ID")
    private long transactionId;

    @Column(name = "MONEY_SOURCE")
    @Enumerated(EnumType.STRING)
    private MoneySource source;

    public long getTransactionId() {
        return transactionId;
    }

    public WalletTransactionId setTransactionId(long transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public MoneySource getSource() {
        return source;
    }

    public WalletTransactionId setSource(MoneySource source) {
        this.source = source;
        return this;
    }

}
