package com.clemble.casino.server.bonus.listener;

import com.clemble.casino.money.Money;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.server.KeyGenerator;
import com.clemble.casino.server.bonus.BonusPaymentTransaction;
import com.clemble.casino.server.bonus.BonusService;
import com.clemble.casino.server.event.player.SystemPlayerSocialAddedEvent;

/**
 * Created by mavarazy on 11/30/14.
 */
public class SocialAddedBonusEventListener implements BonusEventListener<SystemPlayerSocialAddedEvent>{

    final private static PaymentBonusSource SOURCE = PaymentBonusSource.social;
    final private static SocialAddedBonusKeyGenerator KEY_GENERATOR = new SocialAddedBonusKeyGenerator();

    final private static class SocialAddedBonusKeyGenerator implements KeyGenerator {

        public String generate(String player, String provider){
            return player + ":" + provider;
        }

    }

    final private Money amount;
    final private BonusService bonusService;

    public SocialAddedBonusEventListener(Money amout, BonusService bonusService) {
        this.amount = amout;
        this.bonusService = bonusService;
    }

    @Override
    public void onEvent(SystemPlayerSocialAddedEvent event) {
        // Step 1. Generating bonus Key
        String player = event.getPlayer();
        String provider = event.getConnection().getProviderId();
        String bonusKey = KEY_GENERATOR.generate(player, provider);
        // Step 2. Processing transaction
        bonusService.process(new BonusPaymentTransaction(event.getPlayer(), bonusKey, SOURCE, amount));
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
