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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class RoundWonByOwnedRuleAspect extends RoundGameAspect<GameEndedEvent> {

    final private Currency currency;
    final private SystemNotificationService systemNotificationService;

    public RoundWonByOwnedRuleAspect(Currency currency, SystemNotificationService systemNotificationService) {
        super(EventSelectors.
            where(new EventTypeSelector(RoundEndedEvent.class)).
            and(new OutcomeTypeSelector<PlayerWonOutcome>(PlayerWonOutcome.class)));
        this.currency = checkNotNull(currency);
        this.systemNotificationService = checkNotNull(systemNotificationService);
    }

    @Override
    protected void doEvent(GameEndedEvent event) {
        GameContext<?> context = event.getState().getContext();
        // Step 2. Generating payment transaction
        PaymentTransaction paymentTransaction = new PaymentTransaction().
            setTransactionKey(context.getSessionKey()).
            setTransactionDate(DateTime.now(DateTimeZone.UTC)).
            setSource(new GamePaymentSource(context.getSessionKey(), event.getOutcome()));
        for (GamePlayerContext playerContext : context.getPlayerContexts()) {
            GamePlayerAccount playerAccount = playerContext.getAccount();
            paymentTransaction
                .addOperation(new PaymentOperation(playerContext.getPlayer(), Money.create(currency, playerAccount.getOwned()), Operation.Debit))
                .addOperation(new PaymentOperation(playerContext.getPlayer(), Money.create(currency, playerAccount.getSpent()), Operation.Credit));
        }
        // Step 3. Processing payment transaction
        systemNotificationService.send(new SystemPaymentTransactionRequestEvent(paymentTransaction));

    }


}
