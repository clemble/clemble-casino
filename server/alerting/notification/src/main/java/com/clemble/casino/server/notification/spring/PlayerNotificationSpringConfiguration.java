package com.clemble.casino.server.notification.spring;

import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.notification.controller.PlayerNotificationServiceController;
import com.clemble.casino.server.notification.listener.SystemNotificationAddEventListener;
import com.clemble.casino.server.notification.repository.PlayerNotificationRepository;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 11/29/14.
 */
@Configuration
@Import(value = {MongoSpringConfiguration.class, CommonSpringConfiguration.class})
public class PlayerNotificationSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerNotificationRepository playerNotificationRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(PlayerNotificationRepository.class);
    }

    @Bean
    public PlayerNotificationServiceController playerNotificationServiceController(PlayerNotificationRepository notificationRepository) {
        return new PlayerNotificationServiceController(notificationRepository);
    }

    @Bean
    public SystemNotificationAddEventListener systemNotificationServiceListener(
        @Qualifier("playerNotificationService") ServerNotificationService notificationService,
        PlayerNotificationRepository notificationRepository,
        @Qualifier("playerConnectionClient") PlayerConnectionService connectionService,
        SystemNotificationServiceListener notificationServiceListener) {
        SystemNotificationAddEventListener notificationAddEventListener = new SystemNotificationAddEventListener(connectionService, notificationRepository, notificationService);
        notificationServiceListener.subscribe(notificationAddEventListener);
        return notificationAddEventListener;
    }

}
