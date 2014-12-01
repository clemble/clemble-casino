package com.clemble.casino.server.bonus;

import com.clemble.casino.money.Operation;
import com.clemble.casino.payment.*;
import com.clemble.casino.money.Money;
import com.clemble.casino.player.PlayerAware;

import java.util.Date;

public class BonusPaymentTransaction implements PlayerAware, AmountAware, PaymentSourceAware, PaymentTransactionConvertible {

    /**
     * Generated 08/01/14
     */
    private static final long serialVersionUID = 1L;

    final private String transactionKey;
    final private String player;
    final private PaymentSource source;
    final private Money amount;

    public BonusPaymentTransaction(String player, PaymentSource source, Money amount) {
        this.source = source;
        this.transactionKey = source.toTransactionKey(player);
        this.player = player;
        this.amount = amount;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public Money getAmount() {
        return amount;
    }

    @Override
    public PaymentSource getSource() {
        return source;
    }

    @Override
    public String getTransactionKey(){
        return transactionKey;
    }

    public PaymentTransaction toTransaction() {
        return new PaymentTransaction().
            setTransactionKey(transactionKey).
            setTransactionDate(new Date()).
            addOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, amount, Operation.Credit)).
            addOperation(new PaymentOperation(player, amount, Operation.Debit)).
            setSource(source);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        result = prime * result + ((player == null) ? 0 : player.hashCode());
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
        BonusPaymentTransaction other = (BonusPaymentTransaction) obj;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (source != other.source)
            return false;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "bonusTransaction:" + player + ":" + source + ":" + amount;
    }

}
