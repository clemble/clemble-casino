package com.clemble.casino.server.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.configuration.NotificationConfigurationService;
import com.clemble.casino.configuration.ResourceLocationService;
import com.clemble.casino.configuration.ResourceLocations;
import com.clemble.casino.configuration.ServerRegistryConfiguration;
import com.clemble.casino.game.Game;

public class SimpleResourceLocationService implements ResourceLocationService {

    final private NotificationConfigurationService notificationConfigurationService;
    final private ServerRegistryConfiguration serverRegistryConfiguration;
    final private List<Game> gameLocations;

    public SimpleResourceLocationService(NotificationConfigurationService notificationConfigurationService,
            ServerRegistryConfiguration serverRegistryConfiguration,
            List<Game> gameLocations) {
        this.notificationConfigurationService = checkNotNull(notificationConfigurationService);
        this.serverRegistryConfiguration = checkNotNull(serverRegistryConfiguration);
        this.gameLocations = checkNotNull(gameLocations);
    }

    @Override
    public ResourceLocations getResources(String playerId) {
        return new ResourceLocations(notificationConfigurationService.get(playerId), serverRegistryConfiguration, gameLocations);
    }

}
