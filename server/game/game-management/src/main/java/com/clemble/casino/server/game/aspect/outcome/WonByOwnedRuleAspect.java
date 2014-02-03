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

public class WonByOwnedRuleAspect extends BasicGameAspect<GameMatchEndedEvent<?>> {

    final private Currency currency;
    final private ServerPaymentTransactionService transactionService;

    public WonByOwnedRuleAspect(Currency currency, ServerPaymentTransactionService transactionService) {
        super(new EventTypeSelector(GameMatchEndedEvent.class));
        this.currency = checkNotNull(currency);
        this.transactionService = checkNotNull(transactionService);
    }

    @Override
    public void doEvent(GameMatchEndedEvent<?> event) {
        // TODO Auto-generated method stub
        GameOutcome outcome = event.getOutcome();
        if (outcome instanceof PlayerWonOutcome) {
            // Step 2. Generating payment transaction
            PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionKey(event.getSession().toPaymentTransactionKey())
                .setTransactionDate(new Date());
            for (MatchGamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
                GamePlayerAccount playerAccount = playerContext.getAccount();
                paymentTransaction
                    .addPaymentOperation(new PaymentOperation(playerContext.getPlayer(), Money.create(currency, playerAccount.getOwned()), Operation.Debit))
                    .addPaymentOperation(new PaymentOperation(playerContext.getPlayer(), Money.create(currency, playerAccount.getSpent()), Operation.Credit));
            }
            // Step 3. Processing payment transaction
            transactionService.process(paymentTransaction);
            // Step 4. Specifying payment transaction
            event.setTransaction(paymentTransaction);
        }
    }


}
