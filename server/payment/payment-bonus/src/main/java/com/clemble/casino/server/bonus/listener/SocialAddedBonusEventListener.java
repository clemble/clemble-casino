package com.clemble.casino.server.bonus.listener;

import com.clemble.casino.money.Money;
import com.clemble.casino.payment.PaymentSource;
import com.clemble.casino.payment.bonus.SocialAddedBonusPaymentSource;
import com.clemble.casino.server.bonus.BonusPaymentTransaction;
import com.clemble.casino.server.bonus.BonusService;
import com.clemble.casino.server.event.player.SystemPlayerSocialAddedEvent;

/**
 * Created by mavarazy on 11/30/14.
 */
public class SocialAddedBonusEventListener implements BonusEventListener<SystemPlayerSocialAddedEvent>{

    final private Money amount;
    final private BonusService bonusService;

    public SocialAddedBonusEventListener(Money amount, BonusService bonusService) {
        this.amount = amount;
        this.bonusService = bonusService;
    }

    @Override
    public void onEvent(SystemPlayerSocialAddedEvent event) {
        // Step 1. Generating bonus Key
        String provider = event.getConnection().getProviderId();
        PaymentSource source = new SocialAddedBonusPaymentSource(provider);
        // Step 2. Processing transaction
        bonusService.process(new BonusPaymentTransaction(event.getPlayer(), source, amount));
    }

    @Override
    public String getChannel() {
        return SystemPlayerSocialAddedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerSocialAddedEvent.CHANNEL + " > payment:bonus:social";
    }
}
