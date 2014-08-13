package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Date;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.outcome.DrawOutcome;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class RoundDrawByOwnedRuleAspect extends BasicGameAspect<GameEndedEvent<?>> {

    final private Currency currency;
    final private SystemNotificationService systemNotificationService;

    public RoundDrawByOwnedRuleAspect(Currency currency, SystemNotificationService systemNotificationService) {
        super(new EventTypeSelector(GameEndedEvent.class));
        this.currency = checkNotNull(currency);
        this.systemNotificationService = checkNotNull(systemNotificationService);
    }

    @Override
    public void doEvent(GameEndedEvent<?> event) {
        // TODO Auto-generated method stub
        GameOutcome outcome = event.getOutcome();
        GameContext<?> context = event.getContext();
        if (outcome instanceof DrawOutcome) {
            // Step 2. Generating payment transaction
            PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionKey(context.getSessionKey().toPaymentTransactionKey())
                .setTransactionDate(new Date());
            for (GamePlayerContext playerContext : context.getPlayerContexts()) {
                GamePlayerAccount playerAccount = playerContext.getAccount();
                paymentTransaction
                    .addPaymentOperation(new PaymentOperation(playerContext.getPlayer(), Money.create(currency, playerAccount.getOwned()), Operation.Debit))
                    .addPaymentOperation(new PaymentOperation(playerContext.getPlayer(), Money.create(currency, playerAccount.getSpent()), Operation.Credit));
            }
            // Step 3. Processing payment transaction
            systemNotificationService.notify(new SystemPaymentTransactionRequestEvent(paymentTransaction));
        }
    }

}
