package com.gogomaya.server.game.action.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameOutcome;
import com.gogomaya.server.game.GamePlayerState;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameSessionState;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.PlayerWonOutcome;
import com.gogomaya.server.game.action.GameProcessorListener;
import com.gogomaya.server.game.active.ActivePlayerQueue;
import com.gogomaya.server.game.event.server.GameEvent;
import com.gogomaya.server.game.table.PendingSessionQueue;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.player.wallet.WalletOperation;
import com.gogomaya.server.player.wallet.WalletTransaction;
import com.gogomaya.server.player.wallet.WalletTransactionId;
import com.gogomaya.server.player.wallet.WalletTransactionManager;

public class GamePostProcessorListener<State extends GameState> implements GameProcessorListener<State> {

    final private ActivePlayerQueue activePlayerQueue;
    final private PendingSessionQueue sessionQueue;
    final private WalletTransactionManager walletTransactionManager;

    public GamePostProcessorListener(final ActivePlayerQueue activePlayerQueue, final WalletTransactionManager walletTransactionManager,
            final PendingSessionQueue sessionQueue) {
        this.activePlayerQueue = checkNotNull(activePlayerQueue);
        this.walletTransactionManager = checkNotNull(walletTransactionManager);
        this.sessionQueue = checkNotNull(sessionQueue);
    }

    @Override
    public void beforeMove(final GameSession<State> session, final ClientEvent move) {
    }

    @Override
    public Collection<GameEvent<State>> afterMove(final GameSession<State> session, final Collection<GameEvent<State>> madeMoves) {
        // Step 0. Sanity check
        if (madeMoves == null)
            return madeMoves;
        // Step 1. Processing each step by step
        if (session.getState().complete()) {
            if (session.getSessionState() == GameSessionState.inactive) {
                sessionQueue.invalidate(session.getSession(), session.getSpecification());
            }

            session.setSessionState(GameSessionState.ended);
            for (long player : session.getState().getPlayerIterator().getPlayers())
                activePlayerQueue.markInActive(player);

            GameOutcome outcome = session.getState().getOutcome();
            if (outcome instanceof PlayerWonOutcome) {
                winnerOutcome((PlayerWonOutcome) outcome, session);
            }

        }
        return madeMoves;
    }

    private void winnerOutcome(final PlayerWonOutcome outcome, final GameSession<State> session) {
        long winnerId = outcome.getWinner();
        Currency currency = session.getSpecification().getCurrency();
        // Step 2. Generating wallet transaction
        long totalWinning = 0;
        WalletTransactionId transactionId = new WalletTransactionId().setSource(MoneySource.TicTacToe).setTransactionId(session.getSession());
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