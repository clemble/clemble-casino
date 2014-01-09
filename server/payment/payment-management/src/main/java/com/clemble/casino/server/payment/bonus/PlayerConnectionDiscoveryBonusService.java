package com.clemble.casino.server.payment.bonus;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.event.SystemPlayerConnectionDiscoveredEvent;
import com.clemble.casino.server.payment.BonusPaymentTransaction;

public class PlayerConnectionDiscoveryBonusService implements BonusEventListener<SystemPlayerConnectionDiscoveredEvent>{

    final private static PaymentBonusSource SOURCE = PaymentBonusSource.discovery;

    final private Money amount;
    final private BonusService bonusService;

    public PlayerConnectionDiscoveryBonusService(Money bonusAmount, BonusService bonusService) {
        this.amount = checkNotNull(bonusAmount);
        this.bonusService = checkNotNull(bonusService);
    }

    @Override
    public void onEvent(SystemPlayerConnectionDiscoveredEvent event) {
        // Step 1. Sanity check
        if (event == null)
            return;
        // Step 2. Generating unique bonus for discovery event
        String transactionSource = SOURCE.name() + event.getDiscovered();
        String transaction = event.getPlayer();
        PaymentTransactionKey transactionKey = new PaymentTransactionKey(transactionSource, transaction);
        // Step 3. Processing bonus in transaction system
        bonusService.process(new BonusPaymentTransaction(event.getPlayer(), transactionKey, SOURCE, amount));
    }

    @Override
    public String getChannel() {
        return SystemPlayerConnectionDiscoveredEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return "payment.bonus.autodiscovery";
    }

}