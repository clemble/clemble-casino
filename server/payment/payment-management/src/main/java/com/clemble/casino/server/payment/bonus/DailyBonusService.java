package com.clemble.casino.server.payment.bonus;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.bonus.PaymentBonusKey;
import com.clemble.casino.payment.bonus.PaymentBonusMarker;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.payment.event.BonusPaymentEvent;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.PlayerEnteredEvent;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;
import com.clemble.casino.server.payment.bonus.policy.BonusPolicy;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.repository.payment.PlayerAccountRepository;

public class DailyBonusService implements BonusService<PlayerEnteredEvent> {

    final private static PaymentBonusSource BONUS_SOURCE = PaymentBonusSource.dailybonus;
    final private static DateFormat DATE_FORMAT = new SimpleDateFormat("ddmmyy");

    final private Money bonusAmount;
    final private BonusPolicy bonusPolicy;
    final private PlayerNotificationService notificationService;
    final private PlayerAccountRepository accountRepository;
    final private PaymentTransactionRepository transactionRepository;
    final private ServerPaymentTransactionService transactionServerService;

    public DailyBonusService(
            PlayerNotificationService notificationService,
            PlayerAccountRepository accountRepository,
            PaymentTransactionRepository transactionRepository,
            ServerPaymentTransactionService transactionServerService,
            BonusPolicy bonusPolicy,
            Money bonusAmount) {
        this.bonusAmount = checkNotNull(bonusAmount);
        this.bonusPolicy = checkNotNull(bonusPolicy);
        this.accountRepository = checkNotNull(accountRepository);
        this.transactionServerService = checkNotNull(transactionServerService);
        this.transactionRepository = checkNotNull(transactionRepository);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public void onEvent(String channel, PlayerEnteredEvent event) {
        // Step 1. Sanity check
        if (event == null || channel == null)
            return;
        // Step 2. Checking last bonus marker
        String player = event.getPlayer();
        PaymentBonusKey bonusKey = new PaymentBonusKey(player, BONUS_SOURCE);
        PaymentBonusMarker bonusMarker = new PaymentBonusMarker(bonusKey, DATE_FORMAT.format(new Date()));
        // Step 3. If day passed since last bonus change bonus 
        if (!transactionRepository.exists(bonusMarker.toTransactionKey())) {
            // Step 4. Updating payment transaction
            PaymentTransaction transaction = new PaymentTransaction()
                .setTransactionKey(bonusMarker.toTransactionKey())
                .setTransactionDate(new Date())
                .addPaymentOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, bonusAmount, Operation.Credit))
                .addPaymentOperation(new PaymentOperation(player, bonusAmount, Operation.Debit));
            if(bonusPolicy.eligible(accountRepository.findOne(player), transaction)) {
                // Step 5. Processing new transaction and updating bonus marker
                transactionServerService.process(transaction);
                // Step 6. Sending bonus notification
                notificationService.notify(new BonusPaymentEvent(player, bonusAmount, BONUS_SOURCE));
            }
        }
    }

}
