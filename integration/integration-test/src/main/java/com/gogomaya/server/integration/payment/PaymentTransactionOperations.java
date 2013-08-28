package com.gogomaya.server.integration.payment;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.payment.PaymentTransaction;

public interface PaymentTransactionOperations {

    public PaymentTransaction perform(PaymentTransaction transaction);

    public PaymentTransaction get(Player player, MoneySource source, long transactionId);

    public PaymentTransaction get(Player player, String source, long transactionId);

}
