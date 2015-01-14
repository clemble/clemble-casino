package com.clemble.casino.server.bonus.listener;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Date;

import com.clemble.casino.money.Money;
import com.clemble.casino.payment.bonus.DailyBonusPaymentSource;
import com.clemble.casino.server.event.player.SystemPlayerEnteredEvent;
import com.clemble.casino.server.bonus.BonusPaymentTransaction;
import com.clemble.casino.server.bonus.BonusService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DailyBonusEventListener implements BonusEventListener<SystemPlayerEnteredEvent> {

    final private Money amount;
    final private BonusService bonusService;

    public DailyBonusEventListener(Money bonusAmount, BonusService bonusService) {
        this.amount = checkNotNull(bonusAmount);
        this.bonusService = checkNotNull(bonusService);
    }

    @Override
    public void onEvent(SystemPlayerEnteredEvent event) {
        // Step 1. Sanity check
        if (event == null)
            return;
        // Step 2. Generating unique bonus marker for the day
        DailyBonusPaymentSource paymentSource = new DailyBonusPaymentSource(DateTime.now(DateTimeZone.UTC));
        String transactionKey = paymentSource.toTransactionKey(event.getPlayer());
        // Step 3. Processing bonus in bonusService 
        bonusService.process(new BonusPaymentTransaction(event.getPlayer(), paymentSource, amount));
    }

    @Override
    public String getChannel(){
        return SystemPlayerEnteredEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerEnteredEvent.CHANNEL + " > payment:bonus:daily";
    }

}
