package com.clemble.casino.server.payment.bonus;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.event.SystemPlayerRegisteredEvent;
import com.clemble.casino.server.payment.BonusPaymentTransaction;
public class PlayerRegisterationBonusService implements BonusEventListener<SystemPlayerRegisteredEvent> {

    final private static PaymentBonusSource SOURCE = PaymentBonusSource.registration;

    final private Money bonusAmount;
    final private BonusService bonusService;

    public PlayerRegisterationBonusService(Money bonusAmount, BonusService bonusService) {
        this.bonusAmount = checkNotNull(bonusAmount);
        this.bonusService = checkNotNull(bonusService);
    }

    @Override
    public void onEvent(SystemPlayerRegisteredEvent event) {
        // Step 1. Sanity check
        if (event == null)
            return;
        // Step 2. Checking last bonus marker
        PaymentTransactionKey transactionKey = new PaymentTransactionKey(SOURCE, event.getPlayer());
        // Step 3. If day passed since last bonus change bonus
        bonusService.process(new BonusPaymentTransaction(event.getPlayer(), transactionKey, SOURCE, bonusAmount));
    }

    @Override
    public String getChannel(){
        return SystemPlayerRegisteredEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return "payment.bonus.registration";
    }

}
