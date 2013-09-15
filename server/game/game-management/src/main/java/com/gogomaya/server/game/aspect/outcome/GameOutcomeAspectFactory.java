package com.gogomaya.server.game.aspect.outcome;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.game.GameState;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.aspect.GameAspectFactory;
import com.gogomaya.server.payment.PaymentTransactionServerService;
import com.gogomaya.server.player.presence.PlayerPresenceServerService;

public class GameOutcomeAspectFactory implements GameAspectFactory {

    final private PaymentTransactionServerService paymentTransactionService;
    final private PlayerPresenceServerService playerStateManager;

    public GameOutcomeAspectFactory(PaymentTransactionServerService paymentTransactionService, PlayerPresenceServerService playerStateManager) {
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
        this.playerStateManager = checkNotNull(playerStateManager);
    }

    @Override
    public <T extends GameState> GameAspect<T> construct(GameInitiation initiation) {
        return new GameOutcomeAspect<T>(initiation.getSpecification().getName().getGame(), playerStateManager, paymentTransactionService);
    }

}
