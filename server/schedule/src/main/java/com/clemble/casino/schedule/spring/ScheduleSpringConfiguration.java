package com.clemble.casino.schedule.spring;

import com.clemble.casino.schedule.job.ScheduleJobExecutor;
import com.clemble.casino.schedule.job.ScheduleJobExecutorFactory;
import com.clemble.casino.schedule.listener.ScheduleAddJobListener;
import com.clemble.casino.schedule.listener.ScheduleRemoveJobListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.JsonSpringConfiguration;
import com.clemble.casino.server.spring.common.RabbitSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Created by mavarazy on 11/8/14.
 */
@Configuration
@Import({ CommonSpringConfiguration.class })
public class ScheduleSpringConfiguration implements SpringConfiguration {

    @Autowired
    public Environment env;

    @Bean
    public ScheduleJobExecutor jobExecutor(ObjectMapper objectMapper, SystemNotificationService notificationService){
        return new ScheduleJobExecutor(objectMapper, notificationService);
    }

    @Bean
    public ScheduleAddJobListener scheduleAddJobListener(
        ObjectMapper objectMapper,
        Scheduler scheduler,
        SystemNotificationServiceListener notificationServiceListener) {
        ScheduleAddJobListener scheduleAddJobListener = new ScheduleAddJobListener(objectMapper, scheduler);
        notificationServiceListener.subscribe(scheduleAddJobListener);
        return scheduleAddJobListener;
    }

    @Bean
    public ScheduleRemoveJobListener scheduleRemoveJobListener(
        ObjectMapper objectMapper,
        Scheduler scheduler,
        SystemNotificationServiceListener notificationServiceListener) {
        ScheduleRemoveJobListener scheduleRemoveJobListener = new ScheduleRemoveJobListener(scheduler);
        notificationServiceListener.subscribe(scheduleRemoveJobListener);
        return scheduleRemoveJobListener;
    }

    @Bean
    public ScheduleJobExecutorFactory scheduleJobExecutorFactory(ScheduleJobExecutor jobExecutor) {
        return new ScheduleJobExecutorFactory(jobExecutor);
    }



    @Bean(destroyMethod = "shutdown")
    public Scheduler scheduler(ScheduleJobExecutorFactory jobExecutorFactory) throws SchedulerException {
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        if (Arrays.asList(env.getActiveProfiles()).contains("cloud")) {
            schedulerFactory.initialize(getClass().getResourceAsStream("/quartz.cloud.properties"));
        } else {
            schedulerFactory.initialize(getClass().getResourceAsStream("/quartz.properties"));
        }
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.setJobFactory(jobExecutorFactory);
        scheduler.start();
        return scheduler;
    }

}
