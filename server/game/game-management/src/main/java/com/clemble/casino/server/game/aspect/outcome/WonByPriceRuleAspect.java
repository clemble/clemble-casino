package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Date;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class WonByPriceRuleAspect extends BasicGameAspect<GameEndedEvent<?>>{

    final private Money price;
    final private ServerPaymentTransactionService transactionService;

    public WonByPriceRuleAspect(Money price, ServerPaymentTransactionService transactionService) {
        super(new EventTypeSelector(GameEndedEvent.class));
        this.transactionService = checkNotNull(transactionService);
        this.price = checkNotNull(price);
    }

    @Override
    public void doEvent(GameEndedEvent<?> event) {
        GameOutcome outcome = event.getOutcome();
        if (outcome instanceof PlayerWonOutcome) {
            String winnerId = ((PlayerWonOutcome) outcome).getWinner();
            // Step 2. Generating payment transaction
            PaymentTransaction transaction = new PaymentTransaction()
                    .setTransactionKey(event.getSession().toPaymentTransactionKey())
                    .setTransactionDate(new Date());
            for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
                if (!playerContext.getPlayer().equals(winnerId)) {
                    transaction
                        .addPaymentOperation(new PaymentOperation(playerContext.getPlayer(), price, Operation.Credit))
                        .addPaymentOperation(new PaymentOperation(winnerId, price, Operation.Debit));
                }
            }
            // Step 3. Processing payment transaction
            transactionService.process(transaction);
            // Step 4. Specifying transaction in response
            event.setTransaction(transaction);
        }
    }

}
