package com.clemble.casino.server.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.configuration.NotificationConfigurationService;
import com.clemble.casino.configuration.ResourceLocationService;
import com.clemble.casino.configuration.ResourceLocations;
import com.clemble.casino.configuration.ServerRegistryConfiguration;

public class SimpleResourceLocationService implements ResourceLocationService {

    final private NotificationConfigurationService notificationConfigurationService;
    final private ServerRegistryConfiguration serverRegistryConfiguration;

    public SimpleResourceLocationService(NotificationConfigurationService notificationConfigurationService,
            ServerRegistryConfiguration serverRegistryConfiguration) {
        this.notificationConfigurationService = checkNotNull(notificationConfigurationService);
        this.serverRegistryConfiguration = checkNotNull(serverRegistryConfiguration);
    }

    @Override
    public ResourceLocations getResources(String playerId) {
        return new ResourceLocations(notificationConfigurationService.get(playerId), serverRegistryConfiguration);
    }

}
