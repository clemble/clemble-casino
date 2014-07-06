package com.clemble.casino.server.payment.bonus;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Date;

import com.clemble.casino.server.event.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.player.presence.SystemNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.payment.BonusPaymentTransaction;
import com.clemble.casino.server.payment.bonus.policy.BonusPolicy;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class BonusService {

    final private static Logger LOG = LoggerFactory.getLogger(DailyBonusEventListener.class);

    final private BonusPolicy bonusPolicy;
    final private PlayerNotificationService notificationService;
    final private SystemNotificationService systemNotificationService;

    public BonusService(
            BonusPolicy bonusPolicy,
            PlayerNotificationService notificationService,
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
        PaymentTransaction transaction = new PaymentTransaction()
            .setTransactionKey(bonus.getTransactionKey()).setTransactionDate(new Date())
            .addPaymentOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, bonus.getAmount(), Operation.Credit))
            .addPaymentOperation(new PaymentOperation(bonus.getPlayer(), bonus.getAmount(), Operation.Debit));
        // Step 3. Processing new transaction and updating bonus marker
        if(bonusPolicy.eligible(transaction)) {
            // Step 4. Sending bonus notification to system
            systemNotificationService.notify(new SystemPaymentTransactionRequestEvent(transaction));
            // Step 5. Sending bonus notification to player
            notificationService.notify(bonus.toEvent());
        }
        LOG.debug("Finished processing {}", bonus);
    }

}