package com.dragosghinea.royale.internal.utils.time;

import org.quartz.Job;
import org.quartz.Trigger;

public interface RoyaleCronJob extends Job {

    String getJobName();

    String getCronExpression();

    Trigger getTrigger();
}
