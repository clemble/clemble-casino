package com.clemble.casino.server.game.aspect.outcome;

import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.event.server.MatchEndedEvent;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import org.springframework.core.Ordered;

import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;

public class MatchDrawRuleAspectFactory implements MatchGameAspectFactory<MatchEndedEvent> {

    final private ServerPaymentTransactionService transactionService;

    public MatchDrawRuleAspectFactory(ServerPaymentTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public GameAspect<MatchEndedEvent> construct(MatchGameConfiguration configuration, MatchGameContext potContext) {
        // Step 1. Check won rule
        if (configuration.getDrawRule() != null)
            return new MatchDrawRuleAspect(configuration.getPrice().getCurrency(), transactionService);
        // Step 2. Checking values
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
