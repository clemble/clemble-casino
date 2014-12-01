package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Date;

import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.OutcomeTypeSelector;
import com.clemble.casino.game.GamePaymentSource;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.GamePlayerAccount;
import com.clemble.casino.game.lifecycle.management.GamePlayerContext;
import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.lifecycle.management.outcome.Outcome;
import com.clemble.casino.lifecycle.management.outcome.PlayerWonOutcome;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class RoundWonBySpentRuleAspect extends RoundGameAspect<GameEndedEvent> {

    final private Currency currency;
    final private SystemNotificationService systemNotificationService;

    public RoundWonBySpentRuleAspect(Currency currency, SystemNotificationService systemNotificationService) {
        super(EventSelectors.
            where(new EventTypeSelector(RoundEndedEvent.class)).
            and(new OutcomeTypeSelector<PlayerWonOutcome>(PlayerWonOutcome.class)));
        this.currency = currency;
        this.systemNotificationService = checkNotNull(systemNotificationService);
    }

    @Override
    protected void doEvent(GameEndedEvent event) {
        Outcome outcome = event.getOutcome();
        GameContext<?> context = event.getState().getContext();
        if (outcome instanceof PlayerWonOutcome) {
            String winnerId = ((PlayerWonOutcome) outcome).getPlayer();
            // Step 2. Generating payment transaction
            PaymentTransaction transaction = new PaymentTransaction()
                .setTransactionKey(context.getSessionKey())
                .setTransactionDate(new Date())
                .setSource(new GamePaymentSource(context.getSessionKey(), event.getOutcome()));
            for (GamePlayerContext playerContext : context.getPlayerContexts()) {
                GamePlayerAccount playerAccount = playerContext.getAccount();
                if (!playerContext.getPlayer().equals(winnerId)) {
                    Money spent = Money.create(currency, playerAccount.getSpent());
                    transaction
                            .addOperation(new PaymentOperation(playerContext.getPlayer(), spent, Operation.Credit))
                            .addOperation(new PaymentOperation(winnerId, spent, Operation.Debit));
                }
            }
            // Step 3. Processing payment transaction
            systemNotificationService.send(new SystemPaymentTransactionRequestEvent(transaction));
        }
    }

}
