package com.clemble.casino.schedule.job;

import com.clemble.casino.schedule.listener.SystemAddJobScheduleEventListener;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.google.common.base.Preconditions.*;

/**
 * Created by mavarazy on 11/8/14.
 */
public class ScheduleJobExecutor implements Job {

    final private Logger LOG = LoggerFactory.getLogger(SystemAddJobScheduleEventListener.class);

    final private SystemNotificationService notificationService;
    final private ObjectMapper objectMapper;

    public ScheduleJobExecutor(ObjectMapper objectMapper, SystemNotificationService notificationService) {
        this.objectMapper = checkNotNull(objectMapper);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.debug("triggering {} {}", context.getJobDetail().getKey().getGroup(), context.getJobDetail().getKey().getName());
        // Step 1. Fetching job data
        JobDataMap jobData = context.getMergedJobDataMap();
        String eventString = jobData.getString(SystemAddJobScheduleEventListener.EVENT_KEY);
        // Step 2. Reading event
        SystemEvent event = null;
        try {
            event = objectMapper.readValue(eventString, SystemEvent.class);
        } catch (IOException e) {
            LOG.error("Failed to read " + eventString, e);
            return;
        }
        // Step 2. Fetching notification channel
        notificationService.send(event);
    }

}
