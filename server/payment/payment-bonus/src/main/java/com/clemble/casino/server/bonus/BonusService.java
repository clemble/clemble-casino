package com.clemble.casino.server.bonus;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.bonus.listener.DailyBonusEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.server.bonus.policy.BonusPolicy;
import com.clemble.casino.server.player.notification.ServerNotificationService;

public class BonusService {

    final private static Logger LOG = LoggerFactory.getLogger(DailyBonusEventListener.class);

    final private BonusPolicy bonusPolicy;
    final private ServerNotificationService notificationService;
    final private SystemNotificationService systemNotificationService;

    public BonusService(
            BonusPolicy bonusPolicy,
            ServerNotificationService notificationService,
            SystemNotificationService systemNotificationService) {
        this.bonusPolicy = checkNotNull(bonusPolicy);
        this.notificationService = checkNotNull(notificationService);
        this.systemNotificationService = checkNotNull(systemNotificationService);
    }

    public void process(BonusPaymentTransaction bonus) {
        LOG.debug("Start processing {}", bonus);
        // Step 1. Sanity check
        if (bonus == null)
            return;
        // Step 2. Checking last bonus marker
        PaymentTransaction transaction = bonus.toTransaction();
        // Step 3. Processing new transaction and updating bonus marker
        if(bonusPolicy.eligible(transaction)) {
            // Step 4. Sending bonus notification to system
            systemNotificationService.send(new SystemPaymentTransactionRequestEvent(transaction));
            // Step 5. Sending bonus notification to player
            notificationService.send(bonus.toEvent());
        }
        LOG.debug("Finished processing {}", bonus);
    }

}
