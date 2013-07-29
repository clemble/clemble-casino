package com.gogomaya.server.game.aspect.outcome;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.GameAware;
import com.gogomaya.server.game.GamePlayerState;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameSessionState;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.event.server.GameServerEvent;
import com.gogomaya.server.game.outcome.GameOutcome;
import com.gogomaya.server.game.outcome.PlayerWonOutcome;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.payment.PaymentOperation;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.payment.PaymentTransactionId;
import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.player.state.PlayerStateManager;

public class GameOutcomeAspect<State extends GameState> implements GameAspect<State>, GameAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -5737576972775889894L;

    final private Game game;
    final private PlayerStateManager activePlayerQueue;
    final private PaymentTransactionService paymentTransactionService;

    public GameOutcomeAspect(final Game game, final PlayerStateManager activePlayerQueue, final PaymentTransactionService paymentTransactionService) {
        this.game = game;
        this.activePlayerQueue = checkNotNull(activePlayerQueue);
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void beforeMove(final GameSession<State> session, final ClientEvent move) {
    }

    @Override
    public Collection<GameServerEvent<State>> afterMove(final GameSession<State> session, final Collection<GameServerEvent<State>> madeMoves) {
        // Step 0. Sanity check
        if (madeMoves == null)
            return madeMoves;
        // Step 1. Processing each step by step
        if (session.getState().complete()) {
            session.setSessionState(GameSessionState.finished);
            for (long player : session.getState().getPlayerIterator().getPlayers())
                activePlayerQueue.markAvailable(player);

            GameOutcome outcome = session.getState().getOutcome();
            if (outcome instanceof PlayerWonOutcome) {
                winnerOutcome((PlayerWonOutcome) outcome, session);
            }

        }
        return madeMoves;
    }

    private void winnerOutcome(final PlayerWonOutcome outcome, final GameSession<State> session) {
        long winnerId = outcome.getWinner();
        Money price = session.getSpecification().getPrice();
        // Step 2. Generating payment transaction
        PaymentTransactionId transactionId = new PaymentTransactionId().setSource(MoneySource.TicTacToe).setTransactionId(session.getSession());
        PaymentTransaction paymentTransaction = new PaymentTransaction().setTransactionId(transactionId);
        for (GamePlayerState playerState : session.getState().getPlayerStates()) {
            if (playerState.getPlayerId() != winnerId) {
                paymentTransaction.addPaymentOperation(
                        new PaymentOperation().setAmmount(price).setOperation(Operation.Credit).setPlayerId(playerState.getPlayerId())).addPaymentOperation(
                        new PaymentOperation().setAmmount(price).setOperation(Operation.Debit).setPlayerId(winnerId));
            }
        }
        // Step 3. Processing payment transaction
        paymentTransactionService.process(paymentTransaction);
    }

}
