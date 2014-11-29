package com.clemble.casino.server.notification.spring;

import com.clemble.casino.server.notification.repository.PlayerNotificationRepository;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 11/29/14.
 */
@Configuration
@Import(value = {MongoSpringConfiguration.class})
public class PlayerNotificationSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerNotificationRepository playerNotificationRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(PlayerNotificationRepository.class);
    }

}
