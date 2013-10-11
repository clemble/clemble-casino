package com.clemble.casino.server.game.aspect.outcome;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;

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
