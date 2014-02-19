package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.springframework.core.Ordered;

import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class MatchWonRuleAspectFactory implements MatchGameAspectFactory<GameEndedEvent<?>> {

    final private ServerPaymentTransactionService transactionService;

    public MatchWonRuleAspectFactory(ServerPaymentTransactionService transactionService) {
        this.transactionService = checkNotNull(transactionService);
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(MatchGameConfiguration configuration, MatchGameContext context) {
        // Step 1. Checking won rule specified
        if (configuration.getWonRule() == null)
            return null;
        // Step 2. Checking won rule
        switch (configuration.getWonRule()) {
            case price:
                return new MatchWonByPriceRuleAspect(configuration.getPrice(), transactionService);
            case spent:
                return new MatchWonBySpentRuleAspect(configuration.getPrice().getCurrency(), transactionService);
            case owned:
                return new MatchWonByOwnedRuleAspect(configuration.getPrice().getCurrency(), transactionService);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
