package com.gogomaya.payment;

public interface PaymentTransactionOperations {

    public PaymentTransaction getPaymentTransaction(String source, long transactionId);

}
