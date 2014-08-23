package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.controller.GoalJudgeDutyServiceController;
import com.clemble.casino.goal.listener.GoalJudgeDutyCreatorEventListener;
import com.clemble.casino.goal.repository.GoalJudgeDutyRepository;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.net.UnknownHostException;

/**
 * Created by mavarazy on 8/23/14.
 */
@Configuration
public class GoalJudgeDutySpringConfiguration implements SpringConfiguration {

    @Bean
    public GoalJudgeDutyServiceController goalJudgeDutyServiceController(GoalJudgeDutyRepository dutyRepository) {
        return new GoalJudgeDutyServiceController(dutyRepository);
    }

    @Bean
    public MongoRepositoryFactory goalJudgeDutyRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoOperations mongoOperations = new MongoTemplate(mongoClient, "clemble");
        return new MongoRepositoryFactory(mongoOperations);
    }

    @Bean
    public GoalJudgeDutyRepository goalJudgeDutyRepository(@Qualifier("goalJudgeDutyRepositoryFactory") MongoRepositoryFactory goalRepositoryFactory) {
        return goalRepositoryFactory.getRepository(GoalJudgeDutyRepository.class);
    }

    @Bean
    public GoalJudgeDutyCreatorEventListener goalJudgeDutyCreatorEventListener(
            GoalJudgeDutyRepository dutyRepository,
            SystemNotificationService notificationService,
            SystemNotificationServiceListener notificationServiceListener) {
        GoalJudgeDutyCreatorEventListener invitationCreator = new GoalJudgeDutyCreatorEventListener(dutyRepository);
        notificationServiceListener.subscribe(invitationCreator);
        return invitationCreator;
    }

}
