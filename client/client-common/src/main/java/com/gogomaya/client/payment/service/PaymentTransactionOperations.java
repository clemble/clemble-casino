package com.gogomaya.client.payment.service;

import java.util.List;

import com.gogomaya.money.MoneySource;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.player.PlayerAware;

public interface PaymentTransactionOperations extends PlayerAware {

    public PaymentTransaction getPaymentTransaction(MoneySource source, long transactionId);

    public PaymentTransaction getPaymentTransaction(String source, long transactionId);

    public List<PaymentTransaction> listPlayerTransaction();

}
