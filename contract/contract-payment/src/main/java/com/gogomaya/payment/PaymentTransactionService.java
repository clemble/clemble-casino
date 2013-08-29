package com.gogomaya.payment;

public interface PaymentTransactionService {

    public PaymentTransaction getPaymentTransaction(long playerId, String source, long transactionId);

}
