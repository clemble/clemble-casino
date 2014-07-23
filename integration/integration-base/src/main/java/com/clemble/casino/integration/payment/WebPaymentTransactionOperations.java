package com.clemble.casino.integration.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.service.PaymentTransactionService;
import com.clemble.casino.server.player.notification.SystemNotificationService;

public class WebPaymentTransactionOperations  extends AbstractPaymentTransactionOperations {

    final private PaymentTransactionService paymentTransactionService;
    final private SystemNotificationService systemNotificationService;

    public WebPaymentTransactionOperations(
        PaymentTransactionService paymentTransactionService,
        SystemNotificationService systemNotificationService) {
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
        this.systemNotificationService = checkNotNull(systemNotificationService);
    }

    @Override
    public PaymentTransaction get(ClembleCasinoOperations player, String source, String transactionId) {
        return paymentTransactionService.getTransaction(source, transactionId);
    }

}
