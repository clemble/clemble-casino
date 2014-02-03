package com.clemble.casino.server.game.aspect.outcome;

import org.springframework.core.Ordered;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class DrawRuleAspectFactory implements MatchGameAspectFactory<GameMatchEndedEvent<?>> {

    // TODO enable caching for DrawRule
    final private ServerPaymentTransactionService transactionService;

    public DrawRuleAspectFactory(ServerPaymentTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public GameAspect<GameMatchEndedEvent<?>> construct(MatchGameConfiguration configuration, MatchGameContext context) {
        switch (configuration.getDrawRule()) {
        case owned:
            return new DrawByOwnedRuleAspect(configuration.getPrice().getCurrency(), transactionService);
        case spent:
            return DrawBySpentRuleAspect.INSTANCE;
        default:
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
