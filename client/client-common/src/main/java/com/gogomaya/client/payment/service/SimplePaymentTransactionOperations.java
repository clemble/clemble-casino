package com.gogomaya.client.payment.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.money.MoneySource;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.service.PaymentTransactionService;

public class SimplePaymentTransactionOperations implements PaymentTransactionOperations {

    /**
     * 
     */
    private static final long serialVersionUID = -5498822576528068505L;

    final private long playerId;
    final private PaymentTransactionService paymentTransactionService;

    public SimplePaymentTransactionOperations(long playerId, PaymentTransactionService paymentTransactionService) {
        this.playerId = playerId;
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
    }

    @Override
    public PaymentTransaction getPaymentTransaction(MoneySource source, long transactionId) {
        return paymentTransactionService.getPaymentTransaction(playerId, source.name(), transactionId);
    }

    @Override
    public PaymentTransaction getPaymentTransaction(String source, long transactionId) {
        return paymentTransactionService.getPaymentTransaction(playerId, source, transactionId);
    }

    @Override
    public List<PaymentTransaction> listPlayerTransaction() {
        return paymentTransactionService.listPlayerTransaction(playerId);
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

}
