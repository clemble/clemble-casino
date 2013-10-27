package com.clemble.casino.configuration;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.game.Game;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceLocations {

    // Notification server endpoint
    final private NotificationConfiguration notificationConfiguration;

    // Player related endpoint
    final private ServerRegistryConfiguration serverRegistryConfiguration;

    // Games on the server
    final private List<Game> games;

    @JsonCreator
    public ResourceLocations(@JsonProperty("notificationConfiguration") NotificationConfiguration notificationConfiguration,
            @JsonProperty("serverRegistryConfiguration") ServerRegistryConfiguration serverRegistryConfiguration,
            @JsonProperty("games") List<Game> games) {
        this.notificationConfiguration = checkNotNull(notificationConfiguration);
        this.serverRegistryConfiguration = checkNotNull(serverRegistryConfiguration);
        this.games = checkNotNull(games);

    }

    public ServerRegistryConfiguration getServerRegistryConfiguration() {
        return serverRegistryConfiguration;
    }
    
    public NotificationConfiguration getNotificationConfiguration() {
        return notificationConfiguration;
    }

    public List<Game> getGames() {
        return games;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((notificationConfiguration == null) ? 0 : notificationConfiguration.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResourceLocations other = (ResourceLocations) obj;
        if (notificationConfiguration == null) {
            if (other.notificationConfiguration != null)
                return false;
        } else if (!notificationConfiguration.equals(other.notificationConfiguration))
            return false;
        return true;
    }

}