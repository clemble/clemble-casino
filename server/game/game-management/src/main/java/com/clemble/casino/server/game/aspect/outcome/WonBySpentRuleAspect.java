package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Date;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.MatchGamePlayerContext;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class WonBySpentRuleAspect extends BasicGameAspect<GameMatchEndedEvent<?>> {

    final private Currency currency;
    final private ServerPaymentTransactionService transactionService;

    public WonBySpentRuleAspect(Currency currency, ServerPaymentTransactionService transactionService) {
        super(new EventTypeSelector(GameMatchEndedEvent.class));
        this.currency = currency;
        this.transactionService = checkNotNull(transactionService);
    }

    @Override
    public void doEvent(GameMatchEndedEvent<?> event) {
        GameOutcome outcome = event.getOutcome();
        if (outcome instanceof PlayerWonOutcome) {
            String winnerId = ((PlayerWonOutcome) outcome).getWinner();
            // Step 2. Generating payment transaction
            PaymentTransaction transaction = new PaymentTransaction()
                    .setTransactionKey(event.getSession().toPaymentTransactionKey())
                    .setTransactionDate(new Date());
            for (MatchGamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
                GamePlayerAccount playerAccount = playerContext.getAccount();
                if (!playerContext.getPlayer().equals(winnerId)) {
                    Money spent = Money.create(currency, playerAccount.getSpent());
                    transaction
                            .addPaymentOperation(new PaymentOperation(playerContext.getPlayer(), spent, Operation.Credit))
                            .addPaymentOperation(new PaymentOperation(winnerId, spent, Operation.Debit));
                }
            }
            // Step 3. Processing payment transaction
            transactionService.process(transaction);
            // Step 4. Specifying transaction in response
            event.setTransaction(transaction);
        }
    }

}
