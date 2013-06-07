package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.action.impl.GamePostProcessor;
import com.gogomaya.server.game.action.impl.VerificationGameProcessor;
import com.gogomaya.server.game.active.ActivePlayerQueue;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.table.PendingSessionQueue;
import com.gogomaya.server.player.wallet.WalletTransactionManager;

public class GameProcessorFactory<State extends GameState> {

    final private ActivePlayerQueue activePlayerQueue;
    final private PendingSessionQueue sessionQueue;
    final private GameProcessor<State> coreProcessor;
    final private WalletTransactionManager walletTransactionManager;

    public GameProcessorFactory(final ActivePlayerQueue activePlayerQueue, final WalletTransactionManager walletTransactionManager,
            final GameProcessor<State> coreProcessor, final PendingSessionQueue sessionQueue) {
        this.coreProcessor = checkNotNull(coreProcessor);
        this.activePlayerQueue = checkNotNull(activePlayerQueue);
        this.walletTransactionManager = checkNotNull(walletTransactionManager);
        this.sessionQueue = checkNotNull(sessionQueue);
    }

    public GameProcessor<State> create(GameSpecification specification) {
        return new GamePostProcessor<>(activePlayerQueue, walletTransactionManager, sessionQueue, new VerificationGameProcessor<State>(coreProcessor));
    }

}
