package com.gogomaya.server.money;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Table(name = "PLAYER_WALLET_TRANSACTION")
@TypeDef(name = "money", typeClass = MoneyHibernate.class)
public class PlayerMoneyTransaction implements Serializable {

    /**
     * Generated 23/04/2012
     */
    private static final long serialVersionUID = -6146155234799607362L;

    @Id
    @Column(name = "TRANSACTION_ID")
    private long transactionId;

    @Id
    @Column(name = "PLAYER_ID")
    private long playerId;

    @Column(name = "TRANSACTION_TYPE")
    @Enumerated(EnumType.STRING)
    private MoneyTransactionType transactionType;

    @Column(name = "OPERATION")
    @Enumerated(EnumType.STRING)
    private Operation operation;

    @Type(type = "money")
    @Columns(columns = { @Column(name = "CURRENCY"), @Column(name = "AMOUNT") })
    private Money ammount;

    @Column(name = "TRANSACTION_DATE")
    private Date date;

    public PlayerMoneyTransaction() {
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public MoneyTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(MoneyTransactionType transactionType) {
        this.transactionType = transactionType;
    }

}
