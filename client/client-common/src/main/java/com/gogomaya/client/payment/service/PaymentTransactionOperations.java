package com.gogomaya.client.payment.service;

import java.util.List;

import com.gogomaya.money.MoneySource;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.player.PlayerAware;

public interface PaymentTransactionOperations extends PlayerAware {

    public PaymentTransaction getPaymentTransaction(MoneySource source, String transactionId);

    public PaymentTransaction getPaymentTransaction(String source, String transactionId);

    public List<PaymentTransaction> listPlayerTransaction();

}
