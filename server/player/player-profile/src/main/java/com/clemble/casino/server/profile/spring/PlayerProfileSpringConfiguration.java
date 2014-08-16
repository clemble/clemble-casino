package com.clemble.casino.server.profile.spring;

import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.profile.listener.PlayerImageChangedEventListener;
import com.clemble.casino.server.profile.listener.PlayerProfileCreationEventListener;
import com.clemble.casino.server.profile.repository.MongoPlayerProfileRepository;
import com.clemble.casino.server.profile.repository.PlayerImageRedirectRepository;
import com.clemble.casino.server.profile.repository.PlayerProfileRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.profile.controller.PlayerImageServiceController;
import com.clemble.casino.server.profile.controller.PlayerProfileServiceController;
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
    public PlayerProfileCreationEventListener playerProfileRegistrationEventListener(PlayerProfileRepository playerRepository, SystemNotificationService notificationService, SystemNotificationServiceListener serviceListener) {
        PlayerProfileCreationEventListener profileCreationService = new PlayerProfileCreationEventListener(playerRepository, notificationService);
        serviceListener.subscribe(profileCreationService);
        return profileCreationService;
    }

    @Bean
    public PlayerImageChangedEventListener playerImageChangedEventListener(PlayerImageRedirectRepository imageRedirectRepository, SystemNotificationServiceListener serviceListener) {
        PlayerImageChangedEventListener imageChangedEventListener = new PlayerImageChangedEventListener(imageRedirectRepository);
        serviceListener.subscribe(imageChangedEventListener);
        return imageChangedEventListener;
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
    public PlayerImageRedirectRepository playerImageRedirectRepository(@Qualifier("playerProfileRepositoryFactory") MongoRepositoryFactory playerProfileRepositoryFactory) {
        return playerProfileRepositoryFactory.getRepository(PlayerImageRedirectRepository.class);
    }

    @Bean
    public PlayerProfileServiceController playerProfileController(PlayerProfileRepository playerProfileRepository) {
        return new PlayerProfileServiceController(playerProfileRepository);
    }

    @Bean
    public PlayerImageServiceController playerImageController(PlayerImageRedirectRepository imageRedirectRepository) {
        return new PlayerImageServiceController(imageRedirectRepository);
    }

}
