package com.dragosghinea.royale.internal.utils.time.format;

import lombok.Getter;

@Getter
public enum TimeDisplay {

    SECONDS(1, "second", "seconds"),
    MINUTES(60, "minute", "minutes"),
    HOURS(3600, "hour", "hours"),
    DAYS(86400, "day", "days"),
    ;

    private final long numberOfSeconds;
    private final String singularName;
    private final String pluralName;

    TimeDisplay(long numberOfSeconds, String singularName, String pluralName) {
        this.numberOfSeconds = numberOfSeconds;
        this.singularName = singularName;
        this.pluralName = pluralName;
    }

    public long getAmount(long timeInSeconds) {
        return timeInSeconds / numberOfSeconds;
    }

}
