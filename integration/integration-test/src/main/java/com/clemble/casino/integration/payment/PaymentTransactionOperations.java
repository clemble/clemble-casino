package com.clemble.casino.integration.payment;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentTransaction;

public interface PaymentTransactionOperations {

    public PaymentTransaction perform(PaymentTransaction transaction);

    public PaymentTransaction get(Player player, MoneySource source, String transactionId);

    public PaymentTransaction get(Player player, String source, String transactionId);

}
