package com.clemble.casino.integration.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.service.PaymentTransactionService;

public class WebPaymentTransactionOperations  extends AbstractPaymentTransactionOperations {

    final private PaymentTransactionService paymentTransactionController;

    public WebPaymentTransactionOperations(PaymentTransactionService paymentTransactionController) {
        this.paymentTransactionController = checkNotNull(paymentTransactionController);
    }

    @Override
    public PaymentTransaction perform(PaymentTransaction transaction) {
        return paymentTransactionController.process(transaction);
    }

    @Override
    public PaymentTransaction get(Player player, String source, String transactionId) {
        return paymentTransactionController.getPaymentTransaction(player.getPlayer(), source, transactionId);
    }

}
