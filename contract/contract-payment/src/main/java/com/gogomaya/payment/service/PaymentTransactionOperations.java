package com.gogomaya.payment.service;

import com.gogomaya.payment.PaymentTransaction;

public interface PaymentTransactionOperations {

    public PaymentTransaction getPaymentTransaction(String source, long transactionId);

}
