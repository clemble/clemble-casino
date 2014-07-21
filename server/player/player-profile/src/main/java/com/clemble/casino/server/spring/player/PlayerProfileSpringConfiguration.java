package com.clemble.casino.server.spring.player;

import com.clemble.casino.server.player.presence.SystemNotificationService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.clemble.casino.server.player.registration.PlayerProfileCreationEventListener;
import com.clemble.casino.server.repository.player.MongoPlayerProfileRepository;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.web.player.PlayerProfileController;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.net.UnknownHostException;

/**
 * Created by mavarazy on 7/4/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class})
public class PlayerProfileSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerProfileCreationEventListener playerProfileRegistrationEventListener(PlayerProfileRepository playerRepository, SystemNotificationService notificationService, SystemNotificationServiceListener notificationServiceListener) {
        PlayerProfileCreationEventListener profileCreationService = new PlayerProfileCreationEventListener(playerRepository, notificationService);
        notificationServiceListener.subscribe(profileCreationService);
        return profileCreationService;
    }

    @Bean
    public MongoRepositoryFactory playerProfileRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoOperations mongoOperations = new MongoTemplate(mongoClient, "clemble");
        return new MongoRepositoryFactory(mongoOperations);
    }

    @Bean
    public PlayerProfileRepository playerProfileRepository(@Qualifier("playerProfileRepositoryFactory") MongoRepositoryFactory playerProfileRepositoryFactory) {
        return playerProfileRepositoryFactory.getRepository(MongoPlayerProfileRepository.class);
    }

    @Bean
    public PlayerProfileController playerProfileController(PlayerProfileRepository playerProfileRepository) {
        return new PlayerProfileController(playerProfileRepository);
    }

}
