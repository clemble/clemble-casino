package com.clemble.casino.server.payment.listener;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.server.event.payment.SystemPaymentFreezeRequestEvent;
import com.clemble.casino.server.payment.repository.PlayerAccountTemplate;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 16/10/14.
 */
public class SystemPaymentFreezeRequestEventListener implements SystemEventListener<SystemPaymentFreezeRequestEvent> {

    final private PlayerAccountTemplate accountTemplate;
    final private ClembleCasinoValidationService validationService;

    public SystemPaymentFreezeRequestEventListener(
        PlayerAccountTemplate accountTemplate,
        ClembleCasinoValidationService validationService) {
        this.accountTemplate = accountTemplate;
        this.validationService = validationService;
    }

    @Override
    public void onEvent(SystemPaymentFreezeRequestEvent event) {
        // Step 0. Validating transaction
        validationService.validate(event.getTransaction());
        // Step 1. Fetching transaction
        PendingTransaction transaction = event.getTransaction();
        // Step 2. Freezing transaction amount
        accountTemplate.freeze(transaction);
    }

    @Override
    public String getChannel() {
        return SystemPaymentFreezeRequestEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPaymentFreezeRequestEvent.CHANNEL + " > payment:transaction:processor";
    }
}
