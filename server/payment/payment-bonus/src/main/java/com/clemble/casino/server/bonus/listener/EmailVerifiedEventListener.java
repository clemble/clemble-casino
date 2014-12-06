package com.clemble.casino.server.bonus.listener;

import com.clemble.casino.money.Money;
import com.clemble.casino.payment.bonus.EmailVerifiedBonusPaymentSource;
import com.clemble.casino.payment.bonus.RegistrationBonusPaymentSource;
import com.clemble.casino.server.bonus.BonusPaymentTransaction;
import com.clemble.casino.server.bonus.BonusService;
import com.clemble.casino.server.event.email.SystemEmailVerifiedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mavarazy on 12/6/14.
 */
public class EmailVerifiedEventListener implements SystemEventListener<SystemEmailVerifiedEvent>{

    final private static Logger LOG = LoggerFactory.getLogger(EmailVerifiedEventListener.class);

    final private Money bonusAmount;
    final private BonusService bonusService;

    public EmailVerifiedEventListener(Money bonusAmount, BonusService bonusService) {
        this.bonusAmount = bonusAmount;
        this.bonusService = bonusService;
    }

    @Override
    public void onEvent(SystemEmailVerifiedEvent event) {
        LOG.debug("processing {} event", event);
        // Step 1. Sanity check
        if (event == null)
            return;
        // Step 2. Checking last bonus marker
        BonusPaymentTransaction transaction = new BonusPaymentTransaction(event.getPlayer(), EmailVerifiedBonusPaymentSource.INSTANCE, bonusAmount);
        // Step 3. If day passed since last bonus change bonus
        LOG.debug("creating transaction {}", transaction);
        bonusService.process(transaction);
    }

    @Override
    public String getChannel() {
        return SystemEmailVerifiedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemEmailVerifiedEvent.CHANNEL + " > payment:bonus";
    }

}
