package com.clemble.casino.server.game.configuration;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.game.configuration.GameConfigurationAware;
import com.clemble.casino.game.configuration.GameConfigurationKeyAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "GAME_CONFIGURATION")
public class ServerGameConfiguration implements GameConfigurationKeyAware, GameConfigurationAware {

    /**
     * Generated 20/01/14
     */
    private static final long serialVersionUID = -7670016401258035073L;

    @Id
    final private String configurationKey;
    final private GameConfiguration configuration;

    @JsonCreator
    public ServerGameConfiguration(@JsonProperty("configurationKey") String configurationKey, @JsonProperty("configuration") GameConfiguration configuration) {
        this.configurationKey = configurationKey;
        this.configuration = configuration;
    }

    @Override
    public String getConfigurationKey() {
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
