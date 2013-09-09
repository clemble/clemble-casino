package com.gogomaya.server.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.player.notification.PaymentEndpointRegistry;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;

public class SimpleServerRegistryServerService implements ServerRegistryServerService {

    final private PlayerNotificationRegistry playerNotificationRegistry;
    final private PaymentEndpointRegistry paymentEndpointRegistry;

    public SimpleServerRegistryServerService(PlayerNotificationRegistry playerNotificationRegistry, PaymentEndpointRegistry paymentEndpoint) {
        this.playerNotificationRegistry = checkNotNull(playerNotificationRegistry);
        this.paymentEndpointRegistry = checkNotNull(paymentEndpoint);
    }

    @Override
    public PlayerNotificationRegistry getPlayerNotificationRegistry() {
        return playerNotificationRegistry;
    }

    @Override
    public PaymentEndpointRegistry getPaymentEndpointRegistry() {
        return paymentEndpointRegistry;
    }

}
