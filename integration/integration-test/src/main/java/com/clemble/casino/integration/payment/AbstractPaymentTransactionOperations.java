package com.clemble.casino.integration.payment;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentTransaction;

abstract public class AbstractPaymentTransactionOperations implements PaymentTransactionOperations {

    @Override
    final public PaymentTransaction get(Player player, MoneySource source, String transactionId) {
        return get(player, source.name(), transactionId);
    }

}
