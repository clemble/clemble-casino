package com.clemble.casino.schedule.listener;

import com.clemble.casino.server.event.schedule.SystemRemoveJobScheduleEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mavarazy on 11/8/14.
 */
public class ScheduleRemoveJobListener implements SystemEventListener<SystemRemoveJobScheduleEvent>{

    final private Logger LOG = LoggerFactory.getLogger(ScheduleRemoveJobListener.class);
    final private Scheduler scheduler;

    public ScheduleRemoveJobListener(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void onEvent(SystemRemoveJobScheduleEvent event) {
        // Step 1. Generating job key
        JobKey jobKey = new JobKey(event.getKey(), event.getGroup());
        // Step 2. Removing job from scheduler
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            LOG.error("Failed to remove " + jobKey, e);
        }
    }

    @Override
    public String getChannel() {
        return SystemRemoveJobScheduleEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemRemoveJobScheduleEvent.CHANNEL + " > schedule:remove";
    }
}
