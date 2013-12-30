package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

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
public class WonRuleAspectFactory implements GameAspectFactory<GameEndedEvent<?>> {

    final private PaymentTransactionServerService transactionService;

    public WonRuleAspectFactory(PaymentTransactionServerService transactionService) {
        this.transactionService = checkNotNull(transactionService);
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(GameInitiation initiation, GameContext construction) {
        switch (initiation.getSpecification().getWonRule()) {
        case price:
            return new WonByPriceRuleAspect(initiation.getSpecification(), transactionService);
        case spent:
            return new WonBySpentRuleAspect(initiation.getSpecification(), transactionService);
        default:
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        }
    }

}
