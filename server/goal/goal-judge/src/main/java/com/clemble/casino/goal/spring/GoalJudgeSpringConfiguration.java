package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.controller.GoalJudgeInvitationServiceController;
import com.clemble.casino.goal.listener.GoalJudgeInvitationCreatorEventListener;
import com.clemble.casino.goal.repository.GoalJudgeInvitationRepository;
import com.clemble.casino.goal.service.GoalJudgeInvitationService;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.net.UnknownHostException;

/**
 * Created by mavarazy on 8/17/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class})
public class GoalJudgeSpringConfiguration implements SpringConfiguration {

    @Bean
    public GoalJudgeInvitationServiceController judgeInvitationServiceController(GoalJudgeInvitationRepository invitationRepository, SystemNotificationService notificationService) {
        return new GoalJudgeInvitationServiceController(invitationRepository, notificationService);
    }

    @Bean
    public MongoRepositoryFactory goalJudgeRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoOperations mongoOperations = new MongoTemplate(mongoClient, "clemble");
        return new MongoRepositoryFactory(mongoOperations);
    }

    @Bean
    public GoalJudgeInvitationRepository goalJudgeInvitationRepository(@Qualifier("goalJudgeRepositoryFactory") MongoRepositoryFactory goalRepositoryFactory) {
        return goalRepositoryFactory.getRepository(GoalJudgeInvitationRepository.class);
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
