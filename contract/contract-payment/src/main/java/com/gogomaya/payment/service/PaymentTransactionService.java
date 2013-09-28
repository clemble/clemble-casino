package com.gogomaya.payment.service;

import java.util.List;

import com.gogomaya.payment.PaymentTransaction;

public interface PaymentTransactionService {

    public PaymentTransaction getPaymentTransaction(String player, String source, String transactionId);

    public List<PaymentTransaction> listPlayerTransaction(String player);

}
