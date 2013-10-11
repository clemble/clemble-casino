package com.clemble.casino.server.game.aspect.outcome;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionState;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.account.GamePlayerAccount;
import com.clemble.casino.game.event.server.GameServerEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;

public class GameOutcomeAspect<State extends GameState> extends BasicGameAspect<State> implements GameAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -5737576972775889894L;

    final private Game game;
    final private PlayerPresenceServerService activePlayerQueue;
    final private PaymentTransactionServerService paymentTransactionService;

    public GameOutcomeAspect(final Game game, final PlayerPresenceServerService activePlayerQueue, final PaymentTransactionServerService paymentTransactionService) {
        this.game = game;
        this.activePlayerQueue = checkNotNull(activePlayerQueue);
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void afterGame(GameSession<State> session, GameServerEvent<State> madeMoves) {
        session.setSessionState(GameSessionState.finished);
        for (String player : session.getState().getPlayerIterator().getPlayers())
            activePlayerQueue.markOnline(player);

        GameOutcome outcome = session.getState().getOutcome();
        if (outcome instanceof PlayerWonOutcome) {
            String winnerId = ((PlayerWonOutcome) outcome).getWinner();
            Money price = session.getSpecification().getPrice();
            // Step 2. Generating payment transaction
            PaymentTransaction paymentTransaction = new PaymentTransaction().setTransactionKey(session.getSession().toPaymentTransactionKey());
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

}
