package com.gogomaya.server.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.configuration.GameLocation;
import com.gogomaya.configuration.NotificationConfigurationService;
import com.gogomaya.configuration.ResourceLocationService;
import com.gogomaya.configuration.ResourceLocations;

public class SimpleResourceLocationController implements ResourceLocationService {

    final private NotificationConfigurationService notificationConfigurationService;

    final private String playerProfileEndpoint;
    final private String paymentEndpoint;

    final private List<GameLocation> gameLocations;

    public SimpleResourceLocationController(NotificationConfigurationService notificationConfigurationService,
            String playerProfileEndpoint,
            String paymentEndpoint,
            List<GameLocation> gameLocations) {
        this.notificationConfigurationService = checkNotNull(notificationConfigurationService);

        this.paymentEndpoint = checkNotNull(paymentEndpoint);
        this.playerProfileEndpoint = checkNotNull(playerProfileEndpoint);

        this.gameLocations = checkNotNull(gameLocations);
    }

    @Override
    public ResourceLocations getResources(long playerId) {
        return new ResourceLocations(notificationConfigurationService.get(playerId), playerProfileEndpoint, paymentEndpoint, gameLocations);
    }

}
