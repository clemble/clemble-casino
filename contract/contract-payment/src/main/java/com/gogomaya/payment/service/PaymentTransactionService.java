package com.gogomaya.payment.service;

import java.util.List;

import com.gogomaya.payment.PaymentTransaction;

public interface PaymentTransactionService {

    public PaymentTransaction getPaymentTransaction(long playerId, String source, long transactionId);

    public List<PaymentTransaction> listPlayerTransaction(long playerId);

}
