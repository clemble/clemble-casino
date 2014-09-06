package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Date;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class RoundWonBySpentRuleAspect extends GameAspect<GameEndedEvent<?>> {

    final private Currency currency;
    final private SystemNotificationService systemNotificationService;

    public RoundWonBySpentRuleAspect(Currency currency, SystemNotificationService systemNotificationService) {
        super(new EventTypeSelector(GameEndedEvent.class));
        this.currency = currency;
        this.systemNotificationService = checkNotNull(systemNotificationService);
    }

    @Override
    public void doEvent(GameEndedEvent<?> event) {
        GameOutcome outcome = event.getOutcome();
        GameContext<?> context = event.getContext();
        if (outcome instanceof PlayerWonOutcome) {
            String winnerId = ((PlayerWonOutcome) outcome).getWinner();
            // Step 2. Generating payment transaction
            PaymentTransaction transaction = new PaymentTransaction()
                    .setTransactionKey(context.getSessionKey())
                    .setTransactionDate(new Date());
            for (GamePlayerContext playerContext : context.getPlayerContexts()) {
                GamePlayerAccount playerAccount = playerContext.getAccount();
                if (!playerContext.getPlayer().equals(winnerId)) {
                    Money spent = Money.create(currency, playerAccount.getSpent());
                    transaction
                            .addPaymentOperation(new PaymentOperation(playerContext.getPlayer(), spent, Operation.Credit))
                            .addPaymentOperation(new PaymentOperation(winnerId, spent, Operation.Debit));
                }
            }
            // Step 3. Processing payment transaction
            systemNotificationService.notify(new SystemPaymentTransactionRequestEvent(transaction));
        }
    }

}
