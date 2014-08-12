package com.clemble.casino.server.connection.spring;

import com.clemble.casino.server.connection.listener.PlayerDiscoveryNotificationEventListener;
import com.clemble.casino.server.connection.repository.GraphPlayerConnectionsRepository;
import com.clemble.casino.server.connection.repository.MongoPlayerConnectionsRepository;
import com.clemble.casino.server.connection.service.GraphPlayerConnectionService;
import com.clemble.casino.server.connection.service.MongoPlayerConnectionsService;
import com.clemble.casino.server.connection.service.ServerPlayerConnectionService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.connection.listener.PlayerConnectionNetworkPopulateListener;
import com.clemble.casino.server.connection.listener.PlayerConnectionNetworkCreationListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.connection.controller.PlayerConnectionServiceController;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import com.clemble.casino.server.spring.common.BasicNeo4JSpringConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.net.UnknownHostException;

@Configuration
@Import({ CommonSpringConfiguration.class, ServerPlayerConnectionsSpringConfiguration.MongoPlayerConnectionsSpringConfiguration.class })
public class PlayerConnectionSpringConfiguration {

    @Bean
    public PlayerConnectionNetworkCreationListener playerConnectionNetworkCreationListener(ServerPlayerConnectionService playerRepository, SystemNotificationServiceListener notificationServiceListener) {
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

    @Bean
    public PlayerConnectionServiceController playerConnectionController(ServerPlayerConnectionService connectionService) {
        return new PlayerConnectionServiceController(connectionService);
    }

}
