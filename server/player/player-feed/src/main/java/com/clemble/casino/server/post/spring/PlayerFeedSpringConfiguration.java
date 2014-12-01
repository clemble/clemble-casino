package com.clemble.casino.server.post.spring;

import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.post.controller.PlayerFeedServiceController;
import com.clemble.casino.server.post.listener.SystemPostAddEventListener;
import com.clemble.casino.server.post.repository.PlayerPostRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.ConnectionClientSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 11/30/14.
 */
@Configuration
@Import(value = {CommonSpringConfiguration.class, MongoSpringConfiguration.class, ConnectionClientSpringConfiguration.class })
public class PlayerFeedSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerPostRepository playerPostRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(PlayerPostRepository.class);
    }

    @Bean
    public PlayerFeedServiceController playerPostServiceController(
        @Qualifier("playerConnectionClient") PlayerConnectionService connectionService,
        PlayerPostRepository postRepository) {
        return new PlayerFeedServiceController(connectionService, postRepository);
    }

    @Bean
    public SystemPostAddEventListener systemPostAddEventListener(
        @Qualifier("playerNotificationService") ServerNotificationService notificationService,
        PlayerPostRepository postRepository,
        @Qualifier("playerConnectionClient") PlayerConnectionService connectionService,
        SystemNotificationServiceListener notificationServiceListener) {
        SystemPostAddEventListener notificationAddEventListener = new SystemPostAddEventListener(postRepository, connectionService, notificationService);
        notificationServiceListener.subscribe(notificationAddEventListener);
        return notificationAddEventListener;
    }

}
