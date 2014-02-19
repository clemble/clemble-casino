package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Date;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.GamePotEndedEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;

public class PotWonRuleAspect extends BasicGameAspect<GamePotEndedEvent> {

    final private Currency currency;
    final private ServerPaymentTransactionService transactionService;

    public PotWonRuleAspect(Currency currency, ServerPaymentTransactionService transactionService) {
        super(new EventTypeSelector(GamePotEndedEvent.class));
        this.currency = currency;
        this.transactionService = checkNotNull(transactionService);
    }

    @Override
    public void doEvent(GamePotEndedEvent event) {
        GameOutcome outcome = event.getOutcome();
        PotGameContext context = event.getContext();
        if (outcome instanceof PlayerWonOutcome) {
            String winnerId = ((PlayerWonOutcome) outcome).getWinner();
            // Step 2. Generating payment transaction
            PaymentTransaction transaction = new PaymentTransaction()
                    .setTransactionKey(context.getSession().toPaymentTransactionKey())
                    .setTransactionDate(new Date());
            // Step 3. Specifying pot transaction
            transaction.addPaymentOperation(new PaymentOperation(winnerId, Money.create(currency, context.getPot()), Operation.Debit));
            for (GamePlayerContext playerContext : context.getPlayerContexts()) {
                // Step 3.1. Distributing spent and owned entities
                GamePlayerAccount playerAccount = playerContext.getAccount();
                String player = playerContext.getPlayer();
                Money spent = Money.create(currency, playerAccount.getSpent());
                Money owned = Money.create(currency, playerAccount.getOwned());
                transaction
                    .addPaymentOperation(new PaymentOperation(player, spent, Operation.Credit))
                    .addPaymentOperation(new PaymentOperation(player, owned, Operation.Debit));
            }
            // Step 3. Processing payment transaction
            transactionService.process(transaction);
            // Step 4. Specifying transaction in response
            event.setTransaction(transaction);
        }
    }

}
