package com.gogomaya.server.game.aspect.outcome;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.game.GameState;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.aspect.GameAspectFactory;
import com.gogomaya.server.payment.PaymentTransactionProcessingService;
import com.gogomaya.server.player.state.PlayerStateManager;

public class GameOutcomeAspectFactory implements GameAspectFactory {

    final private PaymentTransactionProcessingService paymentTransactionService;
    final private PlayerStateManager playerStateManager;

    public GameOutcomeAspectFactory(PaymentTransactionProcessingService paymentTransactionService, PlayerStateManager playerStateManager) {
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
        this.playerStateManager = checkNotNull(playerStateManager);
    }

    @Override
    public <T extends GameState> GameAspect<T> construct(GameInitiation initiation) {
        return new GameOutcomeAspect<T>(initiation.getSpecification().getName().getGame(), playerStateManager, paymentTransactionService);
    }

}
