package com.dragosghinea.royale.internal.utils.logger;

import lombok.Getter;

@Getter
public enum Level {

    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4),
    FATAL(5);

    private final int level;

    Level(int level) {
        this.level = level;
    }

    public boolean isEnabled(Level level) {
        return this.level <= level.level;
    }
}
