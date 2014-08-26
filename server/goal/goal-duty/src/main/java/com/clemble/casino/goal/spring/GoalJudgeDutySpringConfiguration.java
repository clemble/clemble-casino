package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.controller.GoalJudgeDutyServiceController;
import com.clemble.casino.goal.listener.GoalJudgeDutyCreatorEventListener;
import com.clemble.casino.goal.repository.GoalJudgeDutyRepository;
import com.clemble.casino.goal.task.GoalJudgeDutyDueDateTask;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 8/23/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, MongoSpringConfiguration.class})
public class GoalJudgeDutySpringConfiguration implements SpringConfiguration {

    @Bean
    public GoalJudgeDutyServiceController goalJudgeDutyServiceController(GoalJudgeDutyRepository dutyRepository) {
        return new GoalJudgeDutyServiceController(dutyRepository);
    }

    @Bean
    public GoalJudgeDutyRepository goalJudgeDutyRepository(MongoRepositoryFactory mongoRepositoryFactory) {
        return mongoRepositoryFactory.getRepository(GoalJudgeDutyRepository.class);
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

    @Bean
    public GoalJudgeDutyDueDateTask goalJudgeDutyDueDateTask(SystemNotificationService notificationService, GoalJudgeDutyRepository dutyRepository) {
        GoalJudgeDutyDueDateTask dueDateTask = new GoalJudgeDutyDueDateTask(notificationService, dutyRepository);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("CL > goal:judge:duty:due:date:task").build();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2, threadFactory);
        executorService.scheduleWithFixedDelay(dueDateTask, 15, 60, TimeUnit.SECONDS);
        return dueDateTask;
    }

}
