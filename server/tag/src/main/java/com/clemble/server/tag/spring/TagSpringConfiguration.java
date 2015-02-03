package com.clemble.server.tag.spring;

import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.server.tag.controller.PlayerTagServiceController;
import com.clemble.server.tag.listener.TagSystemGoalReachedEventListener;
import com.clemble.server.tag.listener.TagSystemPlayerCreatedEventListener;
import com.clemble.server.tag.repository.ServerPlayerTagsRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 2/3/15.
 */
@Configuration
@Import({CommonSpringConfiguration.class, MongoSpringConfiguration.class})
public class TagSpringConfiguration implements SpringConfiguration {

    @Bean
    public ServerPlayerTagsRepository serverPlayerTagsRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(ServerPlayerTagsRepository.class);
    }

    @Bean
    public PlayerTagServiceController clembleTagServiceController(ServerPlayerTagsRepository tagsRepository) {
        return new PlayerTagServiceController(tagsRepository);
    }

    @Bean
    public TagSystemPlayerCreatedEventListener tagSystemPlayerCreatedEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        ServerPlayerTagsRepository tagsRepository) {
        TagSystemPlayerCreatedEventListener tagCreationListener = new TagSystemPlayerCreatedEventListener(tagsRepository);
        notificationServiceListener.subscribe(tagCreationListener);
        return tagCreationListener;
    }

    @Bean
    public TagSystemGoalReachedEventListener tagSystemGoalReachedEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        @Qualifier("playerNotificationService") ServerNotificationService notificationService,
        ServerPlayerTagsRepository tagsRepository) {
        TagSystemGoalReachedEventListener tagGoalReachedEventListener = new TagSystemGoalReachedEventListener(tagsRepository, notificationService);
        notificationServiceListener.subscribe(tagGoalReachedEventListener);
        return tagGoalReachedEventListener;
    }

}
