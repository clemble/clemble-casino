package com.gogomaya.server.game.action.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.game.action.GamePlayerState;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameSessionState;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.PlayerWonOutcome;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.active.ActivePlayerQueue;
import com.gogomaya.server.game.event.GameEvent;
import com.gogomaya.server.game.table.PendingSessionQueue;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.player.wallet.WalletOperation;
import com.gogomaya.server.player.wallet.WalletTransaction;
import com.gogomaya.server.player.wallet.WalletTransactionId;
import com.gogomaya.server.player.wallet.WalletTransactionManager;

public class GamePostProcessor<State extends GameState> extends AbstractGameProcessor<State> {

    final private ActivePlayerQueue activePlayerQueue;
    final private PendingSessionQueue sessionQueue;
    final private WalletTransactionManager walletTransactionManager;

    public GamePostProcessor(final ActivePlayerQueue activePlayerQueue, final WalletTransactionManager walletTransactionManager,
            final PendingSessionQueue sessionQueue, final GameProcessor<State> delegate) {
        super(delegate);
        this.activePlayerQueue = checkNotNull(activePlayerQueue);
        this.walletTransactionManager = checkNotNull(walletTransactionManager);
        this.sessionQueue = checkNotNull(sessionQueue);
    }

    @Override
    public void beforMove(final GameSession<State> session, final State state, final GameMove move) {
    }

    @Override
    public Collection<GameEvent<State>> afterMove(final GameSession<State> session, final State state, final Collection<GameEvent<State>> madeMoves) {
        // Step 0. Sanity check
        if (madeMoves == null)
            return madeMoves;
        // Step 1. Processing each step by step
        if (state.complete()) {
            if (session.getSessionState() == GameSessionState.inactive) {
                sessionQueue.invalidate(session.getSessionId(), session.getSpecification());
            }

            session.setSessionState(GameSessionState.ended);
            for (long player : state.getPlayerIterator().getPlayers())
                activePlayerQueue.markInActive(player);

            if (state.getOutcome() instanceof PlayerWonOutcome) {
                winnerOutcome((PlayerWonOutcome) state.getOutcome(), session);
            }

        }
        return madeMoves;
    }

    private void winnerOutcome(final PlayerWonOutcome outcome, final GameSession<State> session) {
        long winnerId = outcome.getWinner();
        Currency currency = session.getSpecification().getCurrency();
        // Step 2. Generating wallet transaction
        long totalWinning = 0;
        WalletTransactionId transactionId = new WalletTransactionId().setSource(MoneySource.TicTacToe).setTransactionId(session.getSessionId());
        WalletTransaction walletTransaction = new WalletTransaction().setTransactionId(transactionId);
        for (GamePlayerState playerState : session.getState().getPlayerStates()) {
            if (playerState.getPlayerId() != winnerId) {
                totalWinning = playerState.getMoneySpent();
                walletTransaction.addWalletOperation(new WalletOperation().setAmmount(Money.create(currency, playerState.getMoneySpent()))
                        .setOperation(Operation.Credit).setPlayerId(playerState.getPlayerId()));
            }
        }
        walletTransaction.addWalletOperation(new WalletOperation().setAmmount(Money.create(currency, totalWinning)).setOperation(Operation.Debit)
                .setPlayerId(winnerId));
        // Step 3. Processing wallet transaction
        walletTransactionManager.process(walletTransaction);
    }

}
