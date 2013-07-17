package com.gogomaya.server.payment;

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
public class PaymentOperation implements Serializable {

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

    public PaymentOperation setPlayerId(long playerId) {
        this.playerId = playerId;
        return this;
    }

    public Money getAmmount() {
        return ammount;
    }

    public PaymentOperation setAmmount(Money ammount) {
        this.ammount = ammount;
        return this;
    }

    public Operation getOperation() {
        return operation;
    }

    public PaymentOperation setOperation(Operation operation) {
        this.operation = operation;
        return this;
    }

    @Override
    public String toString() {
        return "WalletOperation [playerId=" + playerId + ", ammount=" + ammount + ", operation=" + operation + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ammount == null) ? 0 : ammount.hashCode());
        result = prime * result + ((operation == null) ? 0 : operation.hashCode());
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
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
        PaymentOperation other = (PaymentOperation) obj;
        if (ammount == null) {
            if (other.ammount != null)
                return false;
        } else if (!ammount.equals(other.ammount))
            return false;
        if (operation != other.operation)
            return false;
        if (playerId != other.playerId)
            return false;
        return true;
    }

}
