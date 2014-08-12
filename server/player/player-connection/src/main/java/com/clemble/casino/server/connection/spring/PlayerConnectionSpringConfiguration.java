package com.clemble.casino.server.connection.spring;

import com.clemble.casino.server.connection.listener.PlayerDiscoveryNotificationEventListener;
import com.clemble.casino.server.connection.repository.GraphPlayerConnectionsRepository;
import com.clemble.casino.server.connection.service.GraphPlayerConnectionService;
import com.clemble.casino.server.connection.service.ServerPlayerConnectionService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.connection.listener.PlayerConnectionNetworkPopulateListener;
import com.clemble.casino.server.connection.listener.PlayerConnectionNetworkCreationListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.connection.controller.PlayerConnectionServiceController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

import com.clemble.casino.server.spring.common.BasicNeo4JSpringConfiguration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.GraphRepository;

@Configuration
@Import({CommonSpringConfiguration.class})
@EnableNeo4jRepositories(basePackages = "com.clemble.casino.server.connection.repository", includeFilters = { @ComponentScan.Filter(value = GraphRepository.class, type = FilterType.ASSIGNABLE_TYPE) })
public class PlayerConnectionSpringConfiguration extends BasicNeo4JSpringConfiguration {

    public PlayerConnectionSpringConfiguration(){
        setBasePackage("com.clemble.casino.server.connection", "com.clemble.casino.server.game.pending");
    }

    @Bean
    public PlayerConnectionNetworkCreationListener playerSocialNetworkCreationListener(ServerPlayerConnectionService playerRepository, SystemNotificationServiceListener notificationServiceListener) {
        PlayerConnectionNetworkCreationListener networkCreationService = new PlayerConnectionNetworkCreationListener(playerRepository);
        notificationServiceListener.subscribe(networkCreationService);
        return networkCreationService;
    }

    @Bean
    public PlayerConnectionNetworkPopulateListener socialNetworkConnectionCreatorListener(ServerPlayerConnectionService playerRepository, SystemNotificationService notificationService, SystemNotificationServiceListener notificationServiceListener) {
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

    @Override
    public String getFolder() {
        return "target/player_graph";
    }

    @Bean
    public GraphPlayerConnectionService connectionService(GraphPlayerConnectionsRepository connectionRepository) {
        return new GraphPlayerConnectionService(connectionRepository);
    }

    @Bean
    public PlayerConnectionServiceController playerConnectionController(ServerPlayerConnectionService connectionService) {
        return new PlayerConnectionServiceController(connectionService);
    }

}
