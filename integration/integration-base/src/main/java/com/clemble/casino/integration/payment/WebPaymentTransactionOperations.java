package com.clemble.casino.integration.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.service.PaymentTransactionServiceBase;
import com.clemble.casino.server.player.notification.SystemNotificationService;

public class WebPaymentTransactionOperations  extends AbstractPaymentTransactionOperations {

    final private PaymentTransactionServiceBase paymentTransactionService;
    final private SystemNotificationService systemNotificationService;

    public WebPaymentTransactionOperations(
        PaymentTransactionServiceBase paymentTransactionService,
        SystemNotificationService systemNotificationService) {
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
        this.systemNotificationService = checkNotNull(systemNotificationService);
    }

    @Override
    public PaymentTransaction get(ClembleCasinoOperations player, String source, String transactionId) {
        return paymentTransactionService.getTransaction(source, transactionId);
    }

}
