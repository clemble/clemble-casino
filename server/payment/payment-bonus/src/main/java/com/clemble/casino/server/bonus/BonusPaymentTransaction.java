package com.clemble.casino.server.bonus;

import com.clemble.casino.payment.AmountAware;
import com.clemble.casino.payment.PaymentTransactionAware;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.payment.bonus.PaymentBonusSourceAware;
import com.clemble.casino.payment.event.BonusPaymentEvent;
import com.clemble.casino.money.Money;
import com.clemble.casino.player.PlayerAware;

public class BonusPaymentTransaction implements PlayerAware, AmountAware, PaymentBonusSourceAware, PaymentTransactionAware {

    /**
     * Generated 08/01/14
     */
    private static final long serialVersionUID = 1L;

    final private String player;
    final private PaymentBonusSource bonusSource;
    final private PaymentTransactionKey transactionKey;
    final private Money amount;

    public BonusPaymentTransaction(String player, PaymentTransactionKey transactionKey, PaymentBonusSource bonusSource, Money amount) {
        this.bonusSource = bonusSource;
        this.transactionKey = transactionKey;
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
    public PaymentBonusSource getBonusSource() {
        return bonusSource;
    }

    @Override
    public PaymentTransactionKey getTransactionKey(){
        return transactionKey;
    }

    public BonusPaymentEvent toEvent(){
        return new BonusPaymentEvent(player, amount, bonusSource, transactionKey);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((bonusSource == null) ? 0 : bonusSource.hashCode());
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
        if (bonusSource != other.bonusSource)
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
        return "bonusTransaction:" + player + ":" + bonusSource + ":" + amount;
    }

}
