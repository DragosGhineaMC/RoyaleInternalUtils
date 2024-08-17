package com.dragosghinea.royale.internal.utils.time.format;

public interface TimeCountdown {

    String format(long timeInSeconds, boolean useSpaces, TimeDisplay... whatToDisplay);
}
