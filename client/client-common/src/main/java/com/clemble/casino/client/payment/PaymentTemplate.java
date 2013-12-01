package com.clemble.casino.client.payment;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.service.PaymentService;

public class PaymentTemplate implements PaymentOperations {

    private static final long serialVersionUID = -5498822576528068505L;

    final private String player;
    final private PaymentService paymentTransactionService;

    public PaymentTemplate(String player, PaymentService paymentTransactionService) {
        this.player = player;
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
    }

    @Override
    public PlayerAccount getAccount() {
        return paymentTransactionService.get(player);
    }

    @Override
    public PaymentTransaction getPaymentTransaction(MoneySource source, String transactionId) {
        return paymentTransactionService.getPaymentTransaction(source.name(), transactionId);
    }

    @Override
    public PaymentTransaction getPaymentTransaction(String source, String transactionId) {
        return paymentTransactionService.getPaymentTransaction(source, transactionId);
    }

    @Override
    public List<PaymentTransaction> getPaymentTransactions() {
        return paymentTransactionService.getPaymentTransactions(player);
    }

    @Override
    public String getPlayer() {
        return player;
    }

}
