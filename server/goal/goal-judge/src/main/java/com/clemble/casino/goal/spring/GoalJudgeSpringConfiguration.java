package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.controller.GoalJudgeInvitationServiceController;
import com.clemble.casino.goal.repository.GoalJudgeInvitationRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
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
 * Created by mavarazy on 8/17/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class})
public class GoalJudgeSpringConfiguration implements SpringConfiguration {

    @Bean
    public GoalJudgeInvitationServiceController judgeInvitationServiceController(GoalJudgeInvitationRepository invitationRepository) {
        return new GoalJudgeInvitationServiceController(invitationRepository);
    }

    @Bean
    public MongoRepositoryFactory goalJudgeRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoOperations mongoOperations = new MongoTemplate(mongoClient, "clemble");
        return new MongoRepositoryFactory(mongoOperations);
    }

    @Bean
    public GoalJudgeInvitationRepository playerGoalRepository(@Qualifier("goalJudgeRepositoryFactory") MongoRepositoryFactory goalRepositoryFactory) {
        return goalRepositoryFactory.getRepository(GoalJudgeInvitationRepository.class);
    }


}
