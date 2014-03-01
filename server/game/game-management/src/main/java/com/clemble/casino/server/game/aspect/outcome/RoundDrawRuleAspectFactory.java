package com.clemble.casino.server.game.aspect.outcome;

import org.springframework.core.Ordered;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class RoundDrawRuleAspectFactory implements GameAspectFactory<GameEndedEvent<?>, GameContext<?>, GameConfiguration> {

    final private ServerPaymentTransactionService transactionService;

    public RoundDrawRuleAspectFactory(ServerPaymentTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(GameConfiguration configuration, GameContext<?> context) {
        // Step 1. Checking draw rule
        if (configuration.getDrawRule() == null)
            return null;
        // Step 2. Constructing draw rule
        switch (configuration.getDrawRule()) {
            case owned:
                return new RoundDrawByOwnedRuleAspect(configuration.getPrice().getCurrency(), transactionService);
            case spent:
                return RoundDrawBySpentRuleAspect.INSTANCE;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
