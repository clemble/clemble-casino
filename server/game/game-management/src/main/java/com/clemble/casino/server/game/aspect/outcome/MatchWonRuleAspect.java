package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Date;

import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.OutcomeTypeSelector;
import com.clemble.casino.game.GamePaymentSource;
import com.clemble.casino.game.lifecycle.management.GamePlayerAccount;
import com.clemble.casino.game.lifecycle.management.GamePlayerContext;
import com.clemble.casino.game.lifecycle.management.MatchGameContext;
import com.clemble.casino.game.lifecycle.management.event.MatchEndedEvent;
import com.clemble.casino.lifecycle.management.outcome.PlayerWonOutcome;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatchWonRuleAspect extends MatchGameAspect<MatchEndedEvent> {

    final private Logger LOG = LoggerFactory.getLogger(MatchWonRuleAspect.class);

    final private Currency currency;
    final private SystemNotificationService systemNotificationService;

    public MatchWonRuleAspect(Currency currency, SystemNotificationService systemNotificationService) {
        super(EventSelectors.
                where(new EventTypeSelector(MatchEndedEvent.class)).
                and(new OutcomeTypeSelector<PlayerWonOutcome>(PlayerWonOutcome.class)));
        this.currency = currency;
        this.systemNotificationService = checkNotNull(systemNotificationService);
    }

    @Override
    protected void doEvent(MatchEndedEvent event) {
        LOG.debug("Processing {}", event);
        MatchGameContext context = event.getState().getContext();
        String winnerId = ((PlayerWonOutcome) event.getOutcome()).getPlayer();
        // Step 2. Generating payment transaction
        PaymentTransaction transaction = new PaymentTransaction()
                .setTransactionKey(context.getSessionKey())
                .setTransactionDate(DateTime.now(DateTimeZone.UTC))
                .setSource(new GamePaymentSource(context.getSessionKey(), event.getOutcome()));
        // Step 3. Specifying pot transaction
        transaction.addOperation(new PaymentOperation(winnerId, Money.create(currency, context.getPot()), Operation.Debit));
        for (GamePlayerContext playerContext : context.getPlayerContexts()) {
            // Step 3.1. Distributing spent and owned entities
            GamePlayerAccount playerAccount = playerContext.getAccount();
            String player = playerContext.getPlayer();
            Money spent = Money.create(currency, playerAccount.getSpent());
            Money owned = Money.create(currency, playerAccount.getOwned());
            transaction
                .addOperation(new PaymentOperation(player, spent, Operation.Credit))
                .addOperation(new PaymentOperation(player, owned, Operation.Debit));
        }
        // Step 3. Processing payment transaction
        systemNotificationService.send(new SystemPaymentTransactionRequestEvent(transaction));
    }

}
