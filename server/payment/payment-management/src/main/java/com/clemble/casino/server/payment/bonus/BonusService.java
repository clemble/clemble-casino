package com.clemble.casino.server.payment.bonus;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.payment.BonusPaymentTransaction;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;
import com.clemble.casino.server.payment.bonus.policy.BonusPolicy;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.repository.payment.PlayerAccountTemplate;

public class BonusService {

    final private static Logger LOG = LoggerFactory.getLogger(DailyBonusEventListener.class);

    final private BonusPolicy bonusPolicy;
    final private PlayerAccountTemplate accountTemplate;
    final private PaymentTransactionRepository transactionRepository;
    final private ServerPaymentTransactionService transactionServerService;
    final private PlayerNotificationService notificationService;

    public BonusService(
            BonusPolicy bonusPolicy,
            PlayerNotificationService notificationService,
            PlayerAccountTemplate accountRepository,
            PaymentTransactionRepository transactionRepository,
            ServerPaymentTransactionService transactionServerService) {
        this.bonusPolicy = checkNotNull(bonusPolicy);
        this.accountTemplate = checkNotNull(accountRepository);
        this.transactionRepository = checkNotNull(transactionRepository);
        this.transactionServerService = checkNotNull(transactionServerService);
        this.notificationService = checkNotNull(notificationService);
    }

    public void process(BonusPaymentTransaction bonus) {
        LOG.debug("Start processing {}", bonus);
        // Step 1. Sanity check
        if (bonus == null)
            return;
        // Step 2. Checking last bonus marker
        if (!transactionRepository.exists(bonus.getTransactionKey())) {
            // Step 3. Updating payment transaction
            PaymentTransaction transaction = new PaymentTransaction()
                .setTransactionKey(bonus.getTransactionKey()).setTransactionDate(new Date())
                .addPaymentOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, bonus.getAmount(), Operation.Credit))
                .addPaymentOperation(new PaymentOperation(bonus.getPlayer(), bonus.getAmount(), Operation.Debit));
            if(bonusPolicy.eligible(accountTemplate.findOne(bonus.getPlayer()), transaction)) {
                // Step 4. Processing new transaction and updating bonus marker
                transactionServerService.process(transaction);
                // Step 5. Sending bonus notification
                notificationService.notify(bonus.toEvent());
            }
        } else {
            LOG.debug("Transaction already exists {}", bonus);
        }
        LOG.debug("Finished processing {}", bonus);
    }

}
