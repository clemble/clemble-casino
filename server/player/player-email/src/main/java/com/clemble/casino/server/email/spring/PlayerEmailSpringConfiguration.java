package com.clemble.casino.server.email.spring;

import com.clemble.casino.server.email.listener.SystemEmailAddedEventListener;
import com.clemble.casino.server.email.repository.PlayerEmailRepository;
import com.clemble.casino.server.event.email.SystemEmailAddedEvent;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 12/6/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, MongoSpringConfiguration.class} )
public class PlayerEmailSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerEmailRepository playerEmailRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(PlayerEmailRepository.class);
    }

    @Bean
    public SystemEmailAddedEventListener systemEmailAddedEventListener(
        PlayerEmailRepository emailRepository,
        SystemNotificationServiceListener notificationServiceListener) {
        SystemEmailAddedEventListener emailAddedEventListener = new SystemEmailAddedEventListener(emailRepository);
        notificationServiceListener.subscribe(emailAddedEventListener);
        return emailAddedEventListener;
    }

}
