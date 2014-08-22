package com.clemble.casino.server.bonus.listener;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.KeyGenerator;
import com.clemble.casino.server.event.player.SystemPlayerDiscoveredConnectionEvent;
import com.clemble.casino.server.bonus.BonusPaymentTransaction;
import com.clemble.casino.server.bonus.BonusService;

public class DiscoveryBonusEventListener implements BonusEventListener<SystemPlayerDiscoveredConnectionEvent> {

    final private static PaymentBonusSource SOURCE = PaymentBonusSource.discovery;
    final private static DiscoveryBonusKeyGenerator KEY_GENERATOR = new DiscoveryBonusKeyGenerator();

    final private static class DiscoveryBonusKeyGenerator implements KeyGenerator {

        public String generate(String player, String discovered){
            return player + SOURCE + discovered;
        }

    }

    final private Money amount;
    final private BonusService bonusService;

    public DiscoveryBonusEventListener(Money bonusAmount, BonusService bonusService) {
        this.amount = checkNotNull(bonusAmount);
        this.bonusService = checkNotNull(bonusService);
    }

    @Override
    public void onEvent(SystemPlayerDiscoveredConnectionEvent event) {
        // Step 1. Sanity check
        if (event == null)
            return;
        // Step 2. Generating unique bonus for discovery event
        String transactionKey = KEY_GENERATOR.generate(event.getPlayer(), event.getDiscovered());
        // Step 3. Processing bonus in transaction system
        bonusService.process(new BonusPaymentTransaction(event.getPlayer(), transactionKey, SOURCE, amount));
    }

    @Override
    public String getChannel() {
        return SystemPlayerDiscoveredConnectionEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerDiscoveredConnectionEvent.CHANNEL + " > payment:bonus:autodiscovery";
    }

}
