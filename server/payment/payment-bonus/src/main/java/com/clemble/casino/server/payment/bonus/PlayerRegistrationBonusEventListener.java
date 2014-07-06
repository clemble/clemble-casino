package com.clemble.casino.server.payment.bonus;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.event.SystemPlayerCreatedEvent;
import com.clemble.casino.server.payment.BonusPaymentTransaction;

public class PlayerRegistrationBonusEventListener implements BonusEventListener<SystemPlayerCreatedEvent> {
    
    final private static Logger LOG = LoggerFactory.getLogger(PlayerRegistrationBonusEventListener.class);

    final private static PaymentBonusSource SOURCE = PaymentBonusSource.registration;

    final private Money bonusAmount;
    final private BonusService bonusService;

    public PlayerRegistrationBonusEventListener(Money bonusAmount, BonusService bonusService) {
        this.bonusAmount = checkNotNull(bonusAmount);
        this.bonusService = checkNotNull(bonusService);
    }

    @Override
    public void onEvent(SystemPlayerCreatedEvent event) {
        LOG.debug("processing {} event", event);
        // Step 1. Sanity check
        if (event == null)
            return;
        // Step 2. Checking last bonus marker
        PaymentTransactionKey transactionKey = new PaymentTransactionKey(SOURCE, event.getPlayer());
        LOG.debug("creating transaction with key {}", transactionKey);
        // Step 3. If day passed since last bonus change bonus
        BonusPaymentTransaction transaction = new BonusPaymentTransaction(event.getPlayer(), transactionKey, SOURCE, bonusAmount);
        LOG.debug("creating transaction {}", transaction);
        bonusService.process(transaction);
    }

    @Override
    public String getChannel(){
        return SystemPlayerCreatedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return "payment.bonus.registration";
    }

}
