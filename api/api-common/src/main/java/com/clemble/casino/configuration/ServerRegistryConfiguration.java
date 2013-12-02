package com.clemble.casino.configuration;

import com.clemble.casino.DNSBasedServerRegistry;
import com.clemble.casino.ServerRegistry;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerRegistryConfiguration {

    final private ServerRegistry playerNotificationBaseUrl;
    final private ServerRegistry playerRegistry;
    final private ServerRegistry paymentBaseUrl;
    final private ServerRegistry gameRegistry;

    public ServerRegistryConfiguration(String baseUrl) {
        this("notif.%s.player." + baseUrl, "%s.player." + baseUrl, "payment." + baseUrl, baseUrl);
    }

    public ServerRegistryConfiguration(String playerNotificationBaseUrl, String playerBaseUrl, String paymentBaseUrl, String gameBaseUrl) {
        this.playerNotificationBaseUrl = new DNSBasedServerRegistry(3, playerNotificationBaseUrl, playerNotificationBaseUrl, playerNotificationBaseUrl);
        this.paymentBaseUrl = new DNSBasedServerRegistry(3, paymentBaseUrl, paymentBaseUrl, paymentBaseUrl);
        this.playerRegistry = new DNSBasedServerRegistry(3, playerBaseUrl, playerBaseUrl, playerBaseUrl);
        this.gameRegistry = new DNSBasedServerRegistry(3, gameBaseUrl, gameBaseUrl, gameBaseUrl);
    }

    @JsonCreator
    public ServerRegistryConfiguration(@JsonProperty("playerNotificationRegistry") ServerRegistry playerNotificationRegistry,
            @JsonProperty("playerRegistry") ServerRegistry playerRegistry, @JsonProperty("paymentRegistry") ServerRegistry paymentRegistry,
            @JsonProperty("gameRegistry") ServerRegistry gameRegistry) {
        this.playerNotificationBaseUrl = playerNotificationRegistry;
        this.playerRegistry = playerRegistry;
        this.paymentBaseUrl = paymentRegistry;
        this.gameRegistry = gameRegistry;
    }

    public ServerRegistry getPlayerNotificationRegistry() {
        return playerNotificationBaseUrl;
    }

    public ServerRegistry getPlayerRegistry() {
        return playerRegistry;
    }

    public ServerRegistry getPaymentRegistry() {
        return paymentBaseUrl;
    }

    public ServerRegistry getGameRegistry() {
        return gameRegistry;
    }

}
