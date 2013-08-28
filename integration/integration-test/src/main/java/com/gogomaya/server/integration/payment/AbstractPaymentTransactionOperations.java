package com.gogomaya.server.integration.payment;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.payment.PaymentTransaction;

abstract public class AbstractPaymentTransactionOperations implements PaymentTransactionOperations {

    @Override
    final public PaymentTransaction get(Player player, MoneySource source, long transactionId) {
        return get(player, source.name(), transactionId);
    }

}
