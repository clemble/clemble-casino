package com.clemble.casino.server.game.aspect.outcome;

import org.springframework.core.Ordered;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.payment.PaymentTransactionServerService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class DrawRuleAspectFactory implements GameAspectFactory<GameEndedEvent<?>> {

    // TODO enable caching for DrawRule
    final private PaymentTransactionServerService transactionService;

    public DrawRuleAspectFactory(PaymentTransactionServerService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(GameInitiation initiation, GameContext construction) {
        switch (initiation.getSpecification().getDrawRule()) {
        case owned:
            return new DrawByOwnedRuleAspect(transactionService, initiation.getSpecification());
        case spent:
            // No action need to be taken
            return null;
        default:
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
