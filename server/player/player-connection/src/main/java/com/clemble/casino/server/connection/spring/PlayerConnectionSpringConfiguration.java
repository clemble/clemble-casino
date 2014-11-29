package com.clemble.casino.server.connection.spring;

import com.clemble.casino.server.connection.controller.PlayerFriendInvitationServiceController;
import com.clemble.casino.server.connection.listener.PlayerDiscoveryNotificationEventListener;
import com.clemble.casino.server.connection.listener.PlayerGraphCreationListener;
import com.clemble.casino.server.connection.listener.PlayerGraphPopulatorListener;
import com.clemble.casino.server.connection.repository.PlayerFriendInvitationRepository;
import com.clemble.casino.server.connection.service.PlayerGraphService;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.connection.controller.PlayerConnectionServiceController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

@Configuration
@Import({ CommonSpringConfiguration.class, ServerPlayerConnectionsSpringConfiguration.MongoPlayerConnectionsSpringConfiguration.class })
public class PlayerConnectionSpringConfiguration {

    @Bean
    public PlayerGraphCreationListener playerConnectionNetworkCreationListener(PlayerGraphService playerRepository, SystemNotificationServiceListener notificationServiceListener) {
        PlayerGraphCreationListener networkCreationService = new PlayerGraphCreationListener(playerRepository);
        notificationServiceListener.subscribe(networkCreationService);
        return networkCreationService;
    }

    @Bean
    public PlayerGraphPopulatorListener socialNetworkConnectionCreatorListener(PlayerGraphService playerRepository, SystemNotificationService notificationService, SystemNotificationServiceListener notificationServiceListener) {
        PlayerGraphPopulatorListener connectionCreatorListener = new PlayerGraphPopulatorListener(playerRepository, notificationService);
        notificationServiceListener.subscribe(connectionCreatorListener);
        return connectionCreatorListener;
    }

    @Bean
    public PlayerDiscoveryNotificationEventListener playerDiscoveryNotifierEventListener(
        @Qualifier("playerNotificationService") ServerNotificationService notificationService,
        SystemNotificationServiceListener notificationServiceListener) {
        PlayerDiscoveryNotificationEventListener discoveryNotifierEventListener = new PlayerDiscoveryNotificationEventListener(notificationService);
        notificationServiceListener.subscribe(discoveryNotifierEventListener);
        return discoveryNotifierEventListener;
    }

    @Bean
    public PlayerConnectionServiceController playerConnectionController(PlayerGraphService connectionService) {
        return new PlayerConnectionServiceController(connectionService);
    }

    @Bean
    public PlayerFriendInvitationServiceController playerFriendInvitationServiceController(PlayerGraphService graphService, PlayerFriendInvitationRepository invitationRepository) {
        return new PlayerFriendInvitationServiceController(invitationRepository, graphService);
    }

}
