package com.clemble.casino.server.bonus.listener;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.KeyGenerator;
import com.clemble.casino.server.event.player.SystemPlayerEnteredEvent;
import com.clemble.casino.server.bonus.BonusPaymentTransaction;
import com.clemble.casino.server.bonus.BonusService;

public class DailyBonusEventListener implements BonusEventListener<SystemPlayerEnteredEvent> {

    final private static PaymentBonusSource SOURCE = PaymentBonusSource.dailybonus;
    final private static DailyBonusKeyGenerator KEY_GENERATOR = new DailyBonusKeyGenerator();

    public static class DailyBonusKeyGenerator implements KeyGenerator {

        final private static DateFormat DATE_FORMAT = new SimpleDateFormat("ddmmyy");

        public String generate(String player) {
            return player + SOURCE + DATE_FORMAT.format(new Date());
        }

    }

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
        String transactionKey = KEY_GENERATOR.generate(event.getPlayer());
        // Step 3. Processing bonus in bonusService 
        bonusService.process(new BonusPaymentTransaction(event.getPlayer(), transactionKey, SOURCE, amount));
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
