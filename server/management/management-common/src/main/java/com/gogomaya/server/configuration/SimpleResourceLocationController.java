package com.gogomaya.server.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.configuration.NotificationConfigurationService;
import com.gogomaya.configuration.ResourceLocationService;
import com.gogomaya.configuration.ResourceLocations;

public class SimpleResourceLocationController implements ResourceLocationService {

    final private NotificationConfigurationService notificationConfigurationService;

    final private String playerProfileEndpoint;
    final private String gameConstructionEndpoint;
    final private String paymentEndpoint;

    public SimpleResourceLocationController(NotificationConfigurationService notificationConfigurationService,
            String playerProfileEndpoint,
            String gameConstructionEndpoint,
            String paymentEndpoint) {
        this.notificationConfigurationService = checkNotNull(notificationConfigurationService);

        this.paymentEndpoint = checkNotNull(paymentEndpoint);
        this.gameConstructionEndpoint = checkNotNull(gameConstructionEndpoint);
        this.playerProfileEndpoint = checkNotNull(playerProfileEndpoint);
    }

    @Override
    public ResourceLocations getResources(long playerId) {
        return new ResourceLocations(notificationConfigurationService.get(playerId), playerProfileEndpoint, paymentEndpoint, gameConstructionEndpoint);
    }

}
