package com.clemble.casino.server.game.rule.bet;

import com.clemble.casino.game.rule.bet.BetRule;
import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class BetRuleHibernate extends AbstractJsonHibernateType<BetRule> {

    public BetRuleHibernate() {
        super(BetRule.class);
    }

}
