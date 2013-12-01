package com.clemble.casino.payment.service;

import java.util.List;

import com.clemble.casino.payment.PaymentTransaction;

public interface PaymentTransactionService {

    public PaymentTransaction getPaymentTransaction(String source, String transactionId);

    public List<PaymentTransaction> getPaymentTransactions(String player);

}
