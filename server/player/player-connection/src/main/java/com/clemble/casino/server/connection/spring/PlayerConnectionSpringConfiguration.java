package com.clemble.casino.server.connection.spring;

import com.clemble.casino.server.connection.listener.PlayerDiscoveryNotificationEventListener;
import com.clemble.casino.server.connection.service.PlayerGraphService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.connection.listener.PlayerConnectionNetworkPopulateListener;
import com.clemble.casino.server.connection.listener.PlayerConnectionNetworkCreationListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.connection.controller.PlayerConnectionServiceController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

@Configuration
@Import({ CommonSpringConfiguration.class, ServerPlayerConnectionsSpringConfiguration.MongoPlayerConnectionsSpringConfiguration.class })
public class PlayerConnectionSpringConfiguration {

    @Bean
    public PlayerConnectionNetworkCreationListener playerConnectionNetworkCreationListener(PlayerGraphService playerRepository, SystemNotificationServiceListener notificationServiceListener) {
        PlayerConnectionNetworkCreationListener networkCreationService = new PlayerConnectionNetworkCreationListener(playerRepository);
        notificationServiceListener.subscribe(networkCreationService);
        return networkCreationService;
    }

    @Bean
    public PlayerConnectionNetworkPopulateListener socialNetworkConnectionCreatorListener(PlayerGraphService playerRepository, SystemNotificationService notificationService, SystemNotificationServiceListener notificationServiceListener) {
        PlayerConnectionNetworkPopulateListener connectionCreatorListener = new PlayerConnectionNetworkPopulateListener(playerRepository, notificationService);
        notificationServiceListener.subscribe(connectionCreatorListener);
        return connectionCreatorListener;
    }

    @Bean
    public PlayerDiscoveryNotificationEventListener playerDiscoveryNotifierEventListener(
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService,
        SystemNotificationServiceListener notificationServiceListener) {
        PlayerDiscoveryNotificationEventListener discoveryNotifierEventListener = new PlayerDiscoveryNotificationEventListener(notificationService);
        notificationServiceListener.subscribe(discoveryNotifierEventListener);
        return discoveryNotifierEventListener;
    }

    @Bean
    public PlayerConnectionServiceController playerConnectionController(PlayerGraphService connectionService) {
        return new PlayerConnectionServiceController(connectionService);
    }

}
