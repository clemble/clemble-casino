package com.gogomaya.server.integration.payment;

import com.gogomaya.money.MoneySource;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.server.integration.player.Player;

public interface PaymentTransactionOperations {

    public PaymentTransaction perform(PaymentTransaction transaction);

    public PaymentTransaction get(Player player, MoneySource source, String transactionId);

    public PaymentTransaction get(Player player, String source, String transactionId);

}
