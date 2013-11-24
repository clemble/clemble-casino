package com.clemble.casino.integration.payment;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentTransaction;

public interface PaymentTransactionOperations {

    public PaymentTransaction perform(PaymentTransaction transaction);

    public PaymentTransaction get(ClembleCasinoOperations player, MoneySource source, String transactionId);

    public PaymentTransaction get(ClembleCasinoOperations player, String source, String transactionId);

}
