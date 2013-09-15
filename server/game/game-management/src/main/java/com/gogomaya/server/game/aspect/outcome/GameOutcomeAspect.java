package com.gogomaya.server.game.aspect.outcome;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.game.Game;
import com.gogomaya.game.GameAware;
import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameSessionState;
import com.gogomaya.game.GameState;
import com.gogomaya.game.account.GamePlayerAccount;
import com.gogomaya.game.event.server.GameServerEvent;
import com.gogomaya.game.outcome.GameOutcome;
import com.gogomaya.game.outcome.PlayerWonOutcome;
import com.gogomaya.money.Money;
import com.gogomaya.money.MoneySource;
import com.gogomaya.money.Operation;
import com.gogomaya.payment.PaymentOperation;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PaymentTransactionId;
import com.gogomaya.player.PlayerPresence;
import com.gogomaya.server.game.aspect.BasicGameAspect;
import com.gogomaya.server.payment.PaymentTransactionServerService;
import com.gogomaya.server.player.presence.PlayerPresenceServerService;

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
        for (long player : session.getState().getPlayerIterator().getPlayers())
            activePlayerQueue.markOnline(player);

        GameOutcome outcome = session.getState().getOutcome();
        if (outcome instanceof PlayerWonOutcome) {
            long winnerId = ((PlayerWonOutcome) outcome).getWinner();
            Money price = session.getSpecification().getPrice();
            // Step 2. Generating payment transaction
            PaymentTransactionId transactionId = new PaymentTransactionId().setSource(MoneySource.TicTacToe).setTransactionId(session.getSession());
            PaymentTransaction paymentTransaction = new PaymentTransaction().setTransactionId(transactionId);
            for (GamePlayerAccount playerState : session.getState().getAccount().getPlayerAccounts()) {
                if (playerState.getPlayerId() != winnerId) {
                    paymentTransaction.addPaymentOperation(
                            new PaymentOperation().setAmount(price).setOperation(Operation.Credit).setPlayerId(playerState.getPlayerId())).addPaymentOperation(
                                    new PaymentOperation().setAmount(price).setOperation(Operation.Debit).setPlayerId(winnerId));
                }
            }
            // Step 3. Processing payment transaction
            paymentTransactionService.process(paymentTransaction);
        }
    }

}
