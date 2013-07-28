package com.gogomaya.server.game.aspect.outcome;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.aspect.GameAspectFactory;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.player.state.PlayerStateManager;

public class GameOutcomeAspectFactory implements GameAspectFactory {

    final private PaymentTransactionService paymentTransactionService;
    final private PlayerStateManager playerStateManager;

    public GameOutcomeAspectFactory(PaymentTransactionService paymentTransactionService, PlayerStateManager playerStateManager) {
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
        this.playerStateManager = checkNotNull(playerStateManager);
    }

    @Override
    public <T extends GameState> GameAspect<T> construct(GameSpecification gameSpecification) {
        return new GameOutcomeAspect<T>(playerStateManager, paymentTransactionService);
    }

}
