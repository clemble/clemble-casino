package com.clemble.casino.server.bonus.listener;

import com.clemble.casino.money.Money;
import com.clemble.casino.payment.bonus.EmailVerifiedBonusPaymentSource;
import com.clemble.casino.payment.bonus.PhoneVerifiedBonusPaymentSource;
import com.clemble.casino.server.bonus.BonusPaymentTransaction;
import com.clemble.casino.server.bonus.BonusService;
import com.clemble.casino.server.event.phone.SystemPhoneVerifiedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mavarazy on 12/8/14.
 */
public class PhoneVerifiedEventListener implements BonusEventListener<SystemPhoneVerifiedEvent> {

    final private static Logger LOG = LoggerFactory.getLogger(PhoneVerifiedEventListener.class);

    final private Money bonusAmount;
    final private BonusService bonusService;

    public PhoneVerifiedEventListener(Money bonusAmount, BonusService bonusService) {
        this.bonusAmount = bonusAmount;
        this.bonusService = bonusService;
    }


    @Override
    public void onEvent(SystemPhoneVerifiedEvent event) {
        LOG.debug("processing {} event", event);
        // Step 1. Sanity check
        if (event == null)
            return;
        // Step 2. Checking last bonus marker
        BonusPaymentTransaction transaction = new BonusPaymentTransaction(event.getPlayer(), PhoneVerifiedBonusPaymentSource.INSTANCE, bonusAmount);
        // Step 3. If day passed since last bonus change bonus
        LOG.debug("creating transaction {}", transaction);
        bonusService.process(transaction);
    }

    @Override
    public String getChannel() {
        return SystemPhoneVerifiedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPhoneVerifiedEvent.CHANNEL + " > payment:bonus";
    }

}
