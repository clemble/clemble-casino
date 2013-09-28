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

    final private String player;
    final private PaymentTransactionService paymentTransactionService;

    public SimplePaymentTransactionOperations(String player, PaymentTransactionService paymentTransactionService) {
        this.player = player;
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
    }

    @Override
    public PaymentTransaction getPaymentTransaction(MoneySource source, String transactionId) {
        return paymentTransactionService.getPaymentTransaction(player, source.name(), transactionId);
    }

    @Override
    public PaymentTransaction getPaymentTransaction(String source, String transactionId) {
        return paymentTransactionService.getPaymentTransaction(player, source, transactionId);
    }

    @Override
    public List<PaymentTransaction> listPlayerTransaction() {
        return paymentTransactionService.listPlayerTransaction(player);
    }

    @Override
    public String getPlayer() {
        return player;
    }

}
