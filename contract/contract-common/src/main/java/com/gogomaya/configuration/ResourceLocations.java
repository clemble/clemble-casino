package com.gogomaya.configuration;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceLocations {

    // Notification server endpoint
    final private NotificationConfiguration notificationConfiguration;

    // Player related endpoint
    final private String playerProfileEndpoint;

    // Payment related endpoint
    final private String paymentEndpoing;

    // Game construction related endpoint
    final private String gameConstructionEndpoint;

    @JsonCreator
    public ResourceLocations(@JsonProperty("notificationConfiguration") NotificationConfiguration notificationConfiguration,
            @JsonProperty("playerProfileEndpoint") String playerProfileEndpoint,
            @JsonProperty("paymentEndpoint") String paymentEndpoint,
            @JsonProperty("gameConstructionEndpoint") String gameConstructionEndpoint) {
        this.notificationConfiguration = checkNotNull(notificationConfiguration);
        this.playerProfileEndpoint = checkNotNull(playerProfileEndpoint);
        this.paymentEndpoing = paymentEndpoint;
        this.gameConstructionEndpoint = gameConstructionEndpoint;

    }

    public String getPlayerProfileEndpoint() {
        return playerProfileEndpoint;
    }

    public NotificationConfiguration getNotificationConfiguration() {
        return notificationConfiguration;
    }

    public String getGameConstructionEndpoint() {
        return gameConstructionEndpoint;
    }

    public String getPaymentEndpoing() {
        return paymentEndpoing;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gameConstructionEndpoint == null) ? 0 : gameConstructionEndpoint.hashCode());
        result = prime * result + ((notificationConfiguration == null) ? 0 : notificationConfiguration.hashCode());
        result = prime * result + ((paymentEndpoing == null) ? 0 : paymentEndpoing.hashCode());
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
        if (gameConstructionEndpoint == null) {
            if (other.gameConstructionEndpoint != null)
                return false;
        } else if (!gameConstructionEndpoint.equals(other.gameConstructionEndpoint))
            return false;
        if (notificationConfiguration == null) {
            if (other.notificationConfiguration != null)
                return false;
        } else if (!notificationConfiguration.equals(other.notificationConfiguration))
            return false;
        if (paymentEndpoing == null) {
            if (other.paymentEndpoing != null)
                return false;
        } else if (!paymentEndpoing.equals(other.paymentEndpoing))
            return false;
        if (playerProfileEndpoint == null) {
            if (other.playerProfileEndpoint != null)
                return false;
        } else if (!playerProfileEndpoint.equals(other.playerProfileEndpoint))
            return false;
        return true;
    }

}
