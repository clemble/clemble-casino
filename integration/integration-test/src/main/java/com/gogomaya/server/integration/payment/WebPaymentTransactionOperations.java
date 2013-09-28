package com.gogomaya.server.integration.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.payment.PaymentTransactionController;

public class WebPaymentTransactionOperations  extends AbstractPaymentTransactionOperations {

    final private PaymentTransactionController paymentTransactionController;

    public WebPaymentTransactionOperations(PaymentTransactionController paymentTransactionController) {
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
