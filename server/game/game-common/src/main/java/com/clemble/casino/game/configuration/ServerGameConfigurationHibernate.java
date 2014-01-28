package com.clemble.casino.game.configuration;

import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class ServerGameConfigurationHibernate extends AbstractJsonHibernateType<GameConfiguration> {

    public ServerGameConfigurationHibernate() {
        super(GameConfiguration.class);
    }

}
