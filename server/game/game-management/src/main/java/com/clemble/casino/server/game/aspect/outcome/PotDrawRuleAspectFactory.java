package com.clemble.casino.server.game.aspect.outcome;

import org.springframework.core.Ordered;

import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.GamePotEndedEvent;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.PotGameAspectFactory;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;

public class PotDrawRuleAspectFactory implements PotGameAspectFactory<GamePotEndedEvent>{

    final private ServerPaymentTransactionService transactionService;

    public PotDrawRuleAspectFactory(ServerPaymentTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public GameAspect<GamePotEndedEvent> construct(PotGameConfiguration configuration, PotGameContext potContext) {
        // Step 1. Check won rule
        if (configuration.getDrawRule() != null)
            return new PotDrawRuleAspect(configuration.getPrice().getCurrency(), transactionService);
        // Step 2. Checking values
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
