package com.clemble.casino.server.payment.listener;

import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.server.event.payment.SystemPaymentFreezeRequestEvent;
import com.clemble.casino.server.payment.repository.PlayerAccountTemplate;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 16/10/14.
 */
public class SystemPaymentFreezeRequestEventListener implements SystemEventListener<SystemPaymentFreezeRequestEvent> {

    final private PlayerAccountTemplate accountTemplate;

    public SystemPaymentFreezeRequestEventListener(PlayerAccountTemplate accountTemplate) {
        this.accountTemplate = accountTemplate;
    }

    @Override
    public void onEvent(SystemPaymentFreezeRequestEvent event) {
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
