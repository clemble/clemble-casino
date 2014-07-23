package com.clemble.casino.server.spring.player;

import com.clemble.casino.server.player.connection.listener.PlayerDiscoveryNotifierEventListener;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.player.connection.listener.PlayerSocialNetworkConnectionCreatorListener;
import com.clemble.casino.server.player.connection.listener.PlayerSocialNetworkCreationListener;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.web.player.PlayerConnectionController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

import com.clemble.casino.server.spring.common.BasicNeo4JSpringConfiguration;

@Configuration
@Import({CommonSpringConfiguration.class})
public class PlayerConnectionSpringConfiguration extends BasicNeo4JSpringConfiguration {

    public PlayerConnectionSpringConfiguration(){
        setBasePackage("com.clemble.casino.server.player.social", "com.clemble.casino.server.game.pending");
    }

    @Bean
    public PlayerSocialNetworkCreationListener playerSocialNetworkCreationListener(PlayerSocialNetworkRepository playerRepository, SystemNotificationServiceListener notificationServiceListener) {
        PlayerSocialNetworkCreationListener networkCreationService = new PlayerSocialNetworkCreationListener(playerRepository);
        notificationServiceListener.subscribe(networkCreationService);
        return networkCreationService;
    }

    @Bean
    public PlayerSocialNetworkConnectionCreatorListener socialNetworkConnectionCreatorListener(PlayerSocialNetworkRepository playerRepository, SystemNotificationService notificationService, SystemNotificationServiceListener notificationServiceListener) {
        PlayerSocialNetworkConnectionCreatorListener connectionCreatorListener = new PlayerSocialNetworkConnectionCreatorListener(playerRepository, notificationService);
        notificationServiceListener.subscribe(connectionCreatorListener);
        return connectionCreatorListener;
    }

    @Bean
    public PlayerDiscoveryNotifierEventListener playerDiscoveryNotifierEventListener(
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService,
        SystemNotificationServiceListener notificationServiceListener) {
        PlayerDiscoveryNotifierEventListener discoveryNotifierEventListener = new PlayerDiscoveryNotifierEventListener(notificationService);
        notificationServiceListener.subscribe(discoveryNotifierEventListener);
        return discoveryNotifierEventListener;
    }

    @Override
    public String getFolder() {
        return "target/player_graph";
    }

    @Bean
    public PlayerConnectionController playerConnectionController(PlayerSocialNetworkRepository socialNetworkRepository) {
        return new PlayerConnectionController(socialNetworkRepository);
    }

}
