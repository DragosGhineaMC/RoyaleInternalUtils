package com.dragosghinea.royale.internal.utils.time;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

public class SimpleJobScheduler {

    private final Scheduler scheduler;

    public SimpleJobScheduler() throws SchedulerException {
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();

        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", "RoyaleQuartzScheduler");
        properties.setProperty("org.quartz.threadPool.threadCount", "10");

        schedulerFactory.initialize(properties);

        this.scheduler = schedulerFactory.getScheduler();
        this.scheduler.start();
    }

    public synchronized void scheduleTask(RoyaleCronJob job, String groupName) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(job.getClass())
                                        .withIdentity(job.getJobName(), groupName)
                                        .build();

        scheduler.scheduleJob(jobDetail, job.getTrigger());
    }

    public void scheduleTask(RoyaleCronJob job) throws SchedulerException {
        scheduleTask(job, "default");
    }

    public synchronized boolean cancelTask(String jobName, String groupName) throws SchedulerException {
        return scheduler.deleteJob(new JobKey(jobName, groupName));
    }

    public boolean cancelTask(String jobName) throws SchedulerException {
        return cancelTask(jobName, "default");
    }

    public synchronized boolean cancelGroup(String groupName) throws SchedulerException {
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));

        return scheduler.deleteJobs(new ArrayList<>(jobKeys));
    }

    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
    }
}
