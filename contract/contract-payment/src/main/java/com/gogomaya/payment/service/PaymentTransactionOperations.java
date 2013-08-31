package com.gogomaya.payment.service;

import com.gogomaya.money.MoneySource;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.player.PlayerAware;

public interface PaymentTransactionOperations extends PlayerAware {

    public PaymentTransaction getPaymentTransaction(MoneySource source, long transactionId);

    public PaymentTransaction getPaymentTransaction(String source, long transactionId);

}
