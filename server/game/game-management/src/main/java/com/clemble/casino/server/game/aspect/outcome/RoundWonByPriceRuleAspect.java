package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Date;

import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.GamePlayerContext;
import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.game.lifecycle.management.outcome.GameOutcome;
import com.clemble.casino.game.lifecycle.management.outcome.PlayerWonOutcome;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;

/**
 * Created by mavarazy on 23/12/13.
 */
public class RoundWonByPriceRuleAspect extends GameAspect<GameEndedEvent> {
    
    final private Logger LOG = LoggerFactory.getLogger(RoundWonByPriceRuleAspect.class);

    final private Money price;
    final private SystemNotificationService systemNotificationService;

    public RoundWonByPriceRuleAspect(Money price, SystemNotificationService systemNotificationService) {
        super(new EventTypeSelector(GameEndedEvent.class));
        this.systemNotificationService = checkNotNull(systemNotificationService);
        this.price = checkNotNull(price);
    }

    @Override
    public void doEvent(GameEndedEvent event) {
        LOG.debug("Processing ended event {}", event);
        GameOutcome outcome = event.getOutcome();
        if (outcome instanceof PlayerWonOutcome) {
            GameContext<?> context = event.getState().getContext();
            LOG.debug("Processing won outcome {}", event);
            String winnerId = ((PlayerWonOutcome) outcome).getWinner();
            // Step 2. Generating payment transaction
            PaymentTransaction transaction = new PaymentTransaction()
                    .setTransactionKey(context.getSessionKey())
                    .setTransactionDate(new Date());
            for (GamePlayerContext playerContext : context.getPlayerContexts()) {
                if (!playerContext.getPlayer().equals(winnerId)) {
                    transaction
                        .addPaymentOperation(new PaymentOperation(playerContext.getPlayer(), price, Operation.Credit))
                        .addPaymentOperation(new PaymentOperation(winnerId, price, Operation.Debit));
                }
            }
            // Step 3. Processing payment transaction
            systemNotificationService.notify(new SystemPaymentTransactionRequestEvent(transaction));
        }
    }

}
