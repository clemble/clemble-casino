package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.springframework.core.Ordered;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.construct.ServerGameInitiation;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class WonRuleAspectFactory implements GameAspectFactory<GameMatchEndedEvent<?>> {

    final private ServerPaymentTransactionService transactionService;

    public WonRuleAspectFactory(ServerPaymentTransactionService transactionService) {
        this.transactionService = checkNotNull(transactionService);
    }

    @Override
    public GameAspect<GameMatchEndedEvent<?>> construct(ServerGameInitiation initiation) {
        switch (initiation.getConfiguration().getWonRule()) {
        case price:
            return new WonByPriceRuleAspect(initiation.getConfiguration().getPrice(), transactionService);
        case spent:
            return new WonBySpentRuleAspect(initiation.getConfiguration().getPrice().getCurrency(), transactionService);
        default:
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
