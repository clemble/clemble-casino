package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.controller.GoalJudgeInvitationServiceController;
import com.clemble.casino.goal.listener.GoalJudgeInvitationCreatorEventListener;
import com.clemble.casino.goal.repository.GoalJudgeInvitationRepository;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 8/17/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, MongoSpringConfiguration.class})
public class GoalJudgeSpringConfiguration implements SpringConfiguration {

    @Bean
    public GoalJudgeInvitationServiceController judgeInvitationServiceController(GoalJudgeInvitationRepository invitationRepository, SystemNotificationService notificationService) {
        return new GoalJudgeInvitationServiceController(invitationRepository, notificationService);
    }

    @Bean
    public GoalJudgeInvitationRepository goalJudgeInvitationRepository(MongoRepositoryFactory mongoRepositoryFactory) {
        return mongoRepositoryFactory.getRepository(GoalJudgeInvitationRepository.class);
    }

    @Bean
    public GoalJudgeInvitationCreatorEventListener goalJudgeInvitationCreatorEventListener(
            GoalJudgeInvitationRepository invitationRepository,
            SystemNotificationService notificationService,
            SystemNotificationServiceListener notificationServiceListener) {
        GoalJudgeInvitationCreatorEventListener invitationCreator = new GoalJudgeInvitationCreatorEventListener(invitationRepository, notificationService);
        notificationServiceListener.subscribe(invitationCreator);
        return invitationCreator;
    }

}
