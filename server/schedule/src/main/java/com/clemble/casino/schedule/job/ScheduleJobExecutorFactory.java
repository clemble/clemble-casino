package com.clemble.casino.schedule.job;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * Created by mavarazy on 11/8/14.
 */
public class ScheduleJobExecutorFactory implements JobFactory {

    final private ScheduleJobExecutor jobExecutor;

    public ScheduleJobExecutorFactory(ScheduleJobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        // This factory is supposed to use only singleton scheduler
        return jobExecutor;
    }

}
