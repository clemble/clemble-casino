package com.gogomaya.server.integration.payment;

import com.gogomaya.money.MoneySource;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.server.integration.player.Player;

abstract public class AbstractPaymentTransactionOperations implements PaymentTransactionOperations {

    @Override
    final public PaymentTransaction get(Player player, MoneySource source, String transactionId) {
        return get(player, source.name(), transactionId);
    }

}
