package com.clemble.casino.server.payment.listener;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.server.event.payment.SystemPaymentFreezeRequestEvent;
import com.clemble.casino.server.payment.repository.ServerAccountService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * Created by mavarazy on 16/10/14.
 */
@Validated
public class SystemPaymentFreezeRequestEventListener implements SystemEventListener<SystemPaymentFreezeRequestEvent> {

    final private ServerAccountService accountTemplate;

    public SystemPaymentFreezeRequestEventListener(ServerAccountService accountTemplate) {
        this.accountTemplate = accountTemplate;
    }

    @Override
    public void onEvent(@Valid SystemPaymentFreezeRequestEvent event) {
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
