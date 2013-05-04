package com.gogomaya.server.player.wallet;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.Operation;

@Embeddable
public class WalletOperation implements Serializable {

    /**
     * Generated 05/05/13
     */
    private static final long serialVersionUID = 4480718203883740214L;

    @Column(name = "PLAYER_ID")
    private long playerId;

    @Type(type = "money")
    @Columns(columns = { @Column(name = "CURRENCY"), @Column(name = "AMOUNT") })
    private Money ammount;

    @Enumerated(EnumType.STRING)
    @Column(name = "OPERATION")
    private Operation operation;

    public long getPlayerId() {
        return playerId;
    }

    public WalletOperation setPlayerId(long playerId) {
        this.playerId = playerId;
        return this;
    }

    public Money getAmmount() {
        return ammount;
    }

    public WalletOperation setAmmount(Money ammount) {
        this.ammount = ammount;
        return this;
    }

    public Operation getOperation() {
        return operation;
    }

    public WalletOperation setOperation(Operation operation) {
        this.operation = operation;
        return this;
    }

    @Override
    public String toString() {
        return "WalletOperation [playerId=" + playerId + ", ammount=" + ammount + ", operation=" + operation + "]";
    }

}
