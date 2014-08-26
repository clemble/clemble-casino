package com.clemble.casino.game.configuration;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "GAME_CONFIGURATION")
public class ServerGameConfiguration implements GameConfigurationKeyAware, GameConfigurationAware {

    /**
     * Generated 20/01/14
     */
    private static final long serialVersionUID = -7670016401258035073L;

    @org.springframework.data.annotation.Id
    @EmbeddedId
    private GameConfigurationKey configurationKey;

    @Type(type = "com.clemble.casino.game.configuration.ServerGameConfigurationHibernate")
    @Column(name = "CONFIGURATION", length = 40960)
    private GameConfiguration configuration;

    public ServerGameConfiguration(){
    }

    public ServerGameConfiguration(GameConfigurationKey configurationKey, GameConfiguration configuration) {
        this.configurationKey = configurationKey;
        this.configuration = configuration;
    }

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
