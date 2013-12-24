package com.clemble.casino.server.game.aspect.outcome;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.account.GamePlayerAccount;
import com.clemble.casino.game.outcome.DrawOutcome;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.server.game.aspect.BasicGameManagementAspect;
import com.clemble.casino.server.payment.PaymentTransactionServerService;

import java.util.Date;

/**
 * Created by mavarazy on 23/12/13.
 */
public class DrawByOwnedRuleAspect extends BasicGameManagementAspect {

    final private PaymentTransactionServerService transactionService;

    public DrawByOwnedRuleAspect(PaymentTransactionServerService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public <State extends GameState> void afterGame(GameSession<State> session) {
        GameOutcome outcome = session.getState().getOutcome();
        if (outcome instanceof DrawOutcome) {
            String winnerId = ((PlayerWonOutcome) outcome).getWinner();
            Currency currency = session.getSpecification().getPrice().getCurrency();
            // Step 2. Generating payment transaction
            PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionKey(session.getSession().toPaymentTransactionKey())
                .setTransactionDate(new Date());
            for (GamePlayerAccount playerState : session.getState().getContext().getAccount().getPlayerAccounts()) {
                paymentTransaction
                    .addPaymentOperation(new PaymentOperation(playerState.getPlayer(), Money.create(currency, playerState.getOwned()), Operation.Debit))
                    .addPaymentOperation(new PaymentOperation(playerState.getPlayer(), Money.create(currency, playerState.getSpent()), Operation.Credit));
            }
            // Step 3. Processing payment transaction
            transactionService.process(paymentTransaction);
        }
    }

}
