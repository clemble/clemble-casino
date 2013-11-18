package com.clemble.casino.payment.service;

import java.util.List;

import com.clemble.casino.payment.PaymentTransaction;

public interface PaymentTransactionService {

    // TODO remove
    public PaymentTransaction process(PaymentTransaction paymentTransaction);

    public PaymentTransaction getPaymentTransaction(String source, String transactionId);

    public List<PaymentTransaction> listPlayerTransaction(String player);

}
