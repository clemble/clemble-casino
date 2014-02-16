package com.clemble.casino.game.configuration;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.GameConfigurationAware;
import com.clemble.casino.game.specification.GameConfigurationKey;
import com.clemble.casino.game.specification.GameConfigurationKeyAware;

@Entity
@Table(name = "GAME_CONFIGURATION")
public class ServerGameConfiguration implements GameConfigurationKeyAware, GameConfigurationAware {

    /**
     * Generated 20/01/14
     */
    private static final long serialVersionUID = -7670016401258035073L;

    @EmbeddedId
    private GameConfigurationKey configurationKey;

    @Type(type = "com.clemble.casino.game.configuration.ServerGameConfigurationHibernate")
    @Column(name = "CONFIGURATION", length = 40960)
    private GameConfiguration configuration;

    @Override
    public GameConfigurationKey getConfigurationKey() {
        return configurationKey;
    }

    public void setConfigurationKey(GameConfigurationKey configurationKey) {
        this.configurationKey = configurationKey;
    }

    public GameConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(GameConfiguration configuration) {
        this.configuration = configuration;
    }


}
