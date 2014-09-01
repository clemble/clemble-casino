package com.clemble.casino.server.game.configuration;

import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class GameConfigurationHibernate extends AbstractJsonHibernateType<GameConfiguration> {

    protected GameConfigurationHibernate(Class<GameConfiguration> targetClasss) {
        super(targetClasss);
    }

}
