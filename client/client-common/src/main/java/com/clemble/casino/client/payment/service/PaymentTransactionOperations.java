package com.clemble.casino.client.payment.service;

import java.util.List;

import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.player.PlayerAware;

public interface PaymentTransactionOperations extends PlayerAware {

    public PaymentTransaction getPaymentTransaction(MoneySource source, String transactionId);

    public PaymentTransaction getPaymentTransaction(String source, String transactionId);

    public List<PaymentTransaction> listPlayerTransaction();

}
