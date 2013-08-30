package com.gogomaya.payment.service;

import com.gogomaya.payment.PaymentTransaction;

public interface PaymentTransactionService {

    public PaymentTransaction getPaymentTransaction(long playerId, String source, long transactionId);

}
