package com.clemble.casino.schedule.listener;

import com.clemble.casino.schedule.job.ScheduleJobExecutor;
import com.clemble.casino.server.event.schedule.SystemAddJobScheduleEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

import static com.google.common.base.Preconditions.*;

/**
 * Created by mavarazy on 11/8/14.
 */
public class ScheduleAddJobListener implements SystemEventListener<SystemAddJobScheduleEvent>{

    final public static String EVENT_KEY = "EVENT";

    final private Logger LOG = LoggerFactory.getLogger(ScheduleAddJobListener.class);

    final private Scheduler scheduler;
    final private ObjectMapper objectMapper;

    public ScheduleAddJobListener(ObjectMapper objectMapper, Scheduler scheduler) {
        this.scheduler = checkNotNull(scheduler);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void onEvent(SystemAddJobScheduleEvent event) {
        String sourceEvent;
        try {
            sourceEvent = objectMapper.writeValueAsString(event.getEvent());
        } catch (JsonProcessingException e) {
            LOG.error("Failed to generate event " + event, e);
            return;
        }
        // Step 1. Building job details
        JobDetail jobDetail = newJob(ScheduleJobExecutor.class).
            usingJobData(EVENT_KEY, sourceEvent).
            withIdentity(event.getKey(), event.getGroup()).
            build();
        // Step 2. Adding simple trigger
        Trigger trigger = newTrigger().
            withIdentity(event.getKey(), event.getGroup()).
            startAt(event.getTriggerStartTime()).
            build();
        // Step 3. Actually adding job to Scheduler
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error("Failed to add event " + event, e);
            return;
        }
    }

    @Override
    public String getChannel() {
        return SystemAddJobScheduleEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemAddJobScheduleEvent.CHANNEL + " > schedule:add";
    }

}
