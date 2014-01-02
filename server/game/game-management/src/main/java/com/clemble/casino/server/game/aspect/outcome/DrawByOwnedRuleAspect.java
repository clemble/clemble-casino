package com.clemble.casino.server.game.aspect.outcome;

import java.util.Date;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.outcome.DrawOutcome;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.specification.GameSpecification;
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
public class DrawByOwnedRuleAspect extends BasicGameAspect<GameEndedEvent<?>> {

    final private ServerPaymentTransactionService transactionService;
    final private GameSpecification specification;

    public DrawByOwnedRuleAspect(ServerPaymentTransactionService transactionService, GameSpecification specification) {
        super(new EventTypeSelector(GameEndedEvent.class));
        this.transactionService = transactionService;
        this.specification = specification;
    }

    @Override
    public void doEvent(GameEndedEvent<?> event) {
        // TODO Auto-generated method stub
        GameOutcome outcome = event.getOutcome();
        if (outcome instanceof DrawOutcome) {
            Currency currency = specification.getPrice().getCurrency();
            // Step 2. Generating payment transaction
            PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionKey(event.getSession().toPaymentTransactionKey())
                .setTransactionDate(new Date());
            for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
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
