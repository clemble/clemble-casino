package com.clemble.casino.server.game.configuration;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.game.configuration.GameConfigurationAware;
import com.clemble.casino.game.configuration.GameConfigurationKey;
import com.clemble.casino.game.configuration.GameConfigurationKeyAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "GAME_CONFIGURATION")
public class ServerGameConfiguration implements GameConfigurationKeyAware, GameConfigurationAware {

    /**
     * Generated 20/01/14
     */
    private static final long serialVersionUID = -7670016401258035073L;

    final private GameConfigurationKey configurationKey;
    final private GameConfiguration configuration;

    @JsonCreator
    public ServerGameConfiguration(@JsonProperty("configurationKey") GameConfigurationKey configurationKey, @JsonProperty("configuration") GameConfiguration configuration) {
        this.configurationKey = configurationKey;
        this.configuration = configuration;
    }

    @Override
    public GameConfigurationKey getConfigurationKey() {
        return configurationKey;
    }

    public GameConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerGameConfiguration that = (ServerGameConfiguration) o;

        if (configuration != null ? !configuration.equals(that.configuration) : that.configuration != null)
            return false;
        if (configurationKey != null ? !configurationKey.equals(that.configurationKey) : that.configurationKey != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = configurationKey != null ? configurationKey.hashCode() : 0;
        result = 31 * result + (configuration != null ? configuration.hashCode() : 0);
        return result;
    }
}
