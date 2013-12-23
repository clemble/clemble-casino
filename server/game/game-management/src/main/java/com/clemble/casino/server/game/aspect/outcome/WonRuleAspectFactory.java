package com.clemble.casino.server.game.aspect.outcome;

import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.aspect.GameManagementAspect;
import com.clemble.casino.server.game.aspect.GameManagementAspecteFactory;
import com.clemble.casino.server.payment.PaymentTransactionServerService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class WonRuleAspectFactory implements GameManagementAspecteFactory {

    final private WonByPriceRuleAspect wonByPriceRuleAspect;
    final private WonBySpentRuleAspect wonBySpentRuleAspect;

    public WonRuleAspectFactory(PaymentTransactionServerService transactionService) {
        this.wonByPriceRuleAspect = new WonByPriceRuleAspect(transactionService);
        this.wonBySpentRuleAspect = new WonBySpentRuleAspect(transactionService);
    }

    @Override
    public GameManagementAspect construct(GameInitiation initiation) {
        switch(initiation.getSpecification().getWonRule()) {
            case price:
                return wonByPriceRuleAspect;
            case spent:
                return wonBySpentRuleAspect;
            default:
                throw new IllegalArgumentException();
        }
    }

}
