package com.dragosghinea.royale.internal.utils.time;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleJobScheduler {

    private final Scheduler scheduler;

    public SimpleJobScheduler() throws SchedulerException {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler();
        this.scheduler.start();
    }

    public synchronized void scheduleTask(RoyaleCronJob job) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(job.getClass())
                                        .withIdentity(job.getJobName(), "default")
                                        .build();

        scheduler.scheduleJob(jobDetail, job.getTrigger());
    }

    public synchronized boolean cancelTask(String jobName) throws SchedulerException {
        return scheduler.deleteJob(new JobKey(jobName, "default"));
    }

    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
    }
}
