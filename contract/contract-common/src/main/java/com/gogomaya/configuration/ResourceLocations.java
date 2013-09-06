package com.gogomaya.configuration;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceLocations {

    // Notification server endpoint
    final private NotificationConfiguration notificationConfiguration;

    // Player related endpoint
    final private String playerProfileEndpoint;

    // Payment related endpoint
    final private String paymentEndpoint;

    // Game construction related endpoint
    final private List<GameLocation> gameLocations;

    @JsonCreator
    public ResourceLocations(@JsonProperty("notificationConfiguration") NotificationConfiguration notificationConfiguration,
            @JsonProperty("playerProfileEndpoint") String playerProfileEndpoint,
            @JsonProperty("paymentEndpoint") String paymentEndpoint,
            @JsonProperty("gameLocations") List<GameLocation> gameLocations) {
        this.notificationConfiguration = checkNotNull(notificationConfiguration);
        this.playerProfileEndpoint = checkNotNull(playerProfileEndpoint);
        this.paymentEndpoint = checkNotNull(paymentEndpoint);
        this.gameLocations = checkNotNull(gameLocations);

    }

    public String getPlayerProfileEndpoint() {
        return playerProfileEndpoint;
    }

    public NotificationConfiguration getNotificationConfiguration() {
        return notificationConfiguration;
    }

    public String getPaymentEndpoint() {
        return paymentEndpoint;
    }

    public List<GameLocation> getGameLocations() {
        return gameLocations;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gameLocations == null) ? 0 : gameLocations.hashCode());
        result = prime * result + ((notificationConfiguration == null) ? 0 : notificationConfiguration.hashCode());
        result = prime * result + ((paymentEndpoint == null) ? 0 : paymentEndpoint.hashCode());
        result = prime * result + ((playerProfileEndpoint == null) ? 0 : playerProfileEndpoint.hashCode());
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
        if (gameLocations == null) {
            if (other.gameLocations != null)
                return false;
        } else if (!gameLocations.equals(other.gameLocations))
            return false;
        if (notificationConfiguration == null) {
            if (other.notificationConfiguration != null)
                return false;
        } else if (!notificationConfiguration.equals(other.notificationConfiguration))
            return false;
        if (paymentEndpoint == null) {
            if (other.paymentEndpoint != null)
                return false;
        } else if (!paymentEndpoint.equals(other.paymentEndpoint))
            return false;
        if (playerProfileEndpoint == null) {
            if (other.playerProfileEndpoint != null)
                return false;
        } else if (!playerProfileEndpoint.equals(other.playerProfileEndpoint))
            return false;
        return true;
    }

}