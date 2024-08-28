package com.dragosghinea.royale.internal.utils.time;

import org.quartz.Job;

public interface RoyaleCronJob extends Job {

    String getJobName();

    String getTriggerName();

    String getCronExpression();
}
