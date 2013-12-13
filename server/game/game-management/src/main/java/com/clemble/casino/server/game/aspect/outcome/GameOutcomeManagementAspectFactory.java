package com.clemble.casino.server.game.aspect.outcome;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionState;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.account.GamePlayerAccount;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.server.game.aspect.BasicGameManagementAspect;
import com.clemble.casino.server.game.aspect.GameManagementAspect;
import com.clemble.casino.server.game.aspect.GameManagementAspecteFactory;
import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;

public class GameOutcomeManagementAspectFactory implements GameManagementAspecteFactory {

    final private PlayerPresenceServerService activePlayerQueue;
    final private PaymentTransactionServerService paymentTransactionService;

    public GameOutcomeManagementAspectFactory(final PlayerPresenceServerService activePlayerQueue, final PaymentTransactionServerService paymentTransactionService) {
        this.activePlayerQueue = checkNotNull(activePlayerQueue);
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
    }

    public <State extends GameState> void processPayment(GameSession<State> session) {
        session.setSessionState(GameSessionState.finished);
        for (String player : session.getState().getPlayerIterator().getPlayers())
            activePlayerQueue.markOnline(player);

        GameOutcome outcome = session.getState().getOutcome();
        if (outcome instanceof PlayerWonOutcome) {
            String winnerId = ((PlayerWonOutcome) outcome).getWinner();
            Money price = session.getSpecification().getPrice();
            // Step 2. Generating payment transaction
            PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionKey(session.getSession().toPaymentTransactionKey())
                .setTransactionDate(new Date());
            for (GamePlayerAccount playerState : session.getState().getAccount().getPlayerAccounts()) {
                if (!playerState.getPlayer().equals(winnerId)) {
                    paymentTransaction
                        .addPaymentOperation(
                            new PaymentOperation(playerState.getPlayer(), price, Operation.Credit))
                        .addPaymentOperation(
                            new PaymentOperation(winnerId, price, Operation.Debit));
                }
            }
            // Step 3. Processing payment transaction
            paymentTransactionService.process(paymentTransaction);
        }
    }

    @Override
    public GameManagementAspect construct(GameInitiation initiation) {
        return new BasicGameManagementAspect() {
            @Override
            public <State extends GameState> void afterGame(GameSession<State> state) {
                processPayment(state);
            }
        };
    }

}
