package com.clemble.casino.game;

import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class GameConfigurationHibernate extends AbstractJsonHibernateType<GameConfiguration> {

    protected GameConfigurationHibernate(Class<GameConfiguration> targetClasss) {
        super(targetClasss);
    }

}
