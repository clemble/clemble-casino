package com.clemble.casino.integration.payment;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.service.PaymentTransactionService;
import com.clemble.casino.server.payment.controller.PaymentTransactionServiceController;

public class IntegrationPaymentTransactionService implements PaymentTransactionService {

    private static final long serialVersionUID = -5498822576528068505L;

    final private String player;
    final private PaymentTransactionServiceController paymentTransactionService;

    public IntegrationPaymentTransactionService(
        String player,
        PaymentTransactionServiceController paymentTransactionService) {
        this.player = checkNotNull(player);
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
    }

    @Override
    public List<PaymentTransaction> myTransactions() {
        return paymentTransactionService.myTransactionsBySource(player);
    }

    @Override
    public List<PaymentTransaction> myTransactionsBySource(String source) {
        return paymentTransactionService.myTransactionsBySource(player, source);
    }

    @Override
    public PaymentTransaction getTransaction(String transactionKey) {
        return paymentTransactionService.getTransaction(transactionKey);
    }

    @Override
    public List<PaymentTransaction> getPlayerTransactions(String player) {
        return paymentTransactionService.getPlayerTransactions(player);
    }

}
