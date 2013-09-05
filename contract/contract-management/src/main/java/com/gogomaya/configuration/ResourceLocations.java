package com.gogomaya.configuration;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceLocations {

    // Notification server endpoint
    final private NotificationServerConfiguration notificationServerConfiguration;

    // Player related endpoint
    final private String playerProfileEndpoint;

    // Player session endpoint
    final private String playerSessionEndpoint;

    // Payment related endpoint
    final private String paymentEndpoing;

    // Game construction related endpoint
    final private String gameConstructionEndpoint;

    @JsonCreator
    public ResourceLocations(@JsonProperty("notificationServerConfigurations") NotificationServerConfiguration notificationServerConfigurations,
            @JsonProperty("playerProfileEndpoint") String playerProfileEndpoint,
            @JsonProperty("playerSessionEndpoint") String playerSessionEndpoint,
            @JsonProperty("paymentEndpoint") String paymentEndpoint,
            @JsonProperty("gameConstructionEndpoint") String gameConstructionEndpoint) {
        this.notificationServerConfiguration = checkNotNull(notificationServerConfigurations);
        this.playerProfileEndpoint = checkNotNull(playerProfileEndpoint);
        this.playerSessionEndpoint = checkNotNull(playerSessionEndpoint);
        this.paymentEndpoing = paymentEndpoint;
        this.gameConstructionEndpoint = gameConstructionEndpoint;

    }

    public String getPlayerProfileEndpoint() {
        return playerProfileEndpoint;
    }

    public String getPlayerSessionEndpoint() {
        return playerSessionEndpoint;
    }

    public NotificationServerConfiguration getNotificationServerConfiguration() {
        return notificationServerConfiguration;
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
        result = prime * result + ((notificationServerConfiguration == null) ? 0 : notificationServerConfiguration.hashCode());
        result = prime * result + ((paymentEndpoing == null) ? 0 : paymentEndpoing.hashCode());
        result = prime * result + ((playerProfileEndpoint == null) ? 0 : playerProfileEndpoint.hashCode());
        result = prime * result + ((playerSessionEndpoint == null) ? 0 : playerSessionEndpoint.hashCode());
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
        if (notificationServerConfiguration == null) {
            if (other.notificationServerConfiguration != null)
                return false;
        } else if (!notificationServerConfiguration.equals(other.notificationServerConfiguration))
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
        if (playerSessionEndpoint == null) {
            if (other.playerSessionEndpoint != null)
                return false;
        } else if (!playerSessionEndpoint.equals(other.playerSessionEndpoint))
            return false;
        return true;
    }

}
