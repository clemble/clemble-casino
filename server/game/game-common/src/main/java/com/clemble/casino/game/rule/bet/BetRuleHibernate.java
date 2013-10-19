package com.clemble.casino.game.rule.bet;

import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class BetRuleHibernate extends AbstractJsonHibernateType<BetRule> {

    public BetRuleHibernate() {
        super(BetRule.class);
    }

}
