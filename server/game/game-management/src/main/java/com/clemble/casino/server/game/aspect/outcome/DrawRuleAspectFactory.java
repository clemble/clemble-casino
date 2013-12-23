package com.clemble.casino.server.game.aspect.outcome;

import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.aspect.GameManagementAspect;
import com.clemble.casino.server.game.aspect.GameManagementAspecteFactory;
import com.clemble.casino.server.payment.PaymentTransactionServerService;

/**
 * Created by mavarazy on 23/12/13.
 */
public class DrawRuleAspectFactory implements GameManagementAspecteFactory {

    final private DrawByOwnedRuleAspect drawByOwnedRuleAspect;

    public DrawRuleAspectFactory(PaymentTransactionServerService transactionService) {
        this.drawByOwnedRuleAspect = new DrawByOwnedRuleAspect(transactionService);
    }

    @Override
    public GameManagementAspect construct(GameInitiation initiation) {
        switch (initiation.getSpecification().getDrawRule()) {
            case owned:
                return drawByOwnedRuleAspect;
            default:
                return null;
        }
    }
}
