package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.springframework.core.Ordered;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class WonRuleAspectFactory implements GameAspectFactory<GameEndedEvent<?>, GameContext<?>, GameConfiguration> {

    final private ServerPaymentTransactionService transactionService;

    public WonRuleAspectFactory(ServerPaymentTransactionService transactionService) {
        this.transactionService = checkNotNull(transactionService);
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(GameConfiguration configuration, GameContext<?> context) {
        switch (configuration.getWonRule()) {
        case price:
            return new WonByPriceRuleAspect(configuration.getPrice(), transactionService);
        case spent:
            return new WonBySpentRuleAspect(configuration.getPrice().getCurrency(), transactionService);
        case owned:
            return new WonByOwnedRuleAspect(configuration.getPrice().getCurrency(), transactionService);
        default:
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
