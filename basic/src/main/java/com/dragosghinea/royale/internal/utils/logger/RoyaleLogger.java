package com.dragosghinea.royale.internal.utils.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.bukkit.plugin.Plugin;

import java.nio.file.Paths;

public class RoyaleLogger {
    private final Logger logger;
    private final Plugin plugin;

    public RoyaleLogger(Plugin plugin, String loggerName, String fileName) {
        this(plugin, loggerName, fileName, "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} - %m%n");
    }

    public RoyaleLogger(Plugin plugin, String loggerName, String fileName, String pattern) {
        this(plugin, loggerName, fileName, pattern, Level.INFO);
    }

    public RoyaleLogger(Plugin plugin, String loggerName, String fileName, String pattern, Level level) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();

        String finalFolderName = Paths.get(plugin.getDataFolder().toPath().toString(),
                "logs", fileName, fileName).toString();

        PatternLayout layout = PatternLayout.newBuilder()
                .withPattern(pattern)
                .build();

        RollingFileAppender appender = RollingFileAppender.newBuilder()
                .setName(loggerName+"-Appender")
                .setLayout(layout)
                .withFileName(finalFolderName+".log")
                .withFilePattern(finalFolderName + "-%d{yyyy-MM-dd}.log")
                .withPolicy(TimeBasedTriggeringPolicy.newBuilder().withInterval(1).withModulate(true).build())
                .withPolicy(SizeBasedTriggeringPolicy.createPolicy("10MB"))
                .withStrategy(DefaultRolloverStrategy.newBuilder().withMax("10").build())
                .build();

        appender.start();
        config.addAppender(appender);

        LoggerConfig loggerConfig = new LoggerConfig(loggerName, level, false);
        loggerConfig.addAppender(appender, level, null);

        config.addLogger(loggerName, loggerConfig);
        context.updateLoggers();

        this.logger = LogManager.getLogger(loggerName);
        this.plugin = plugin;
    }

    public void debug(String message) {
        logger.debug(message);
        plugin.getLogger().info("[DEBUG] " + message);
    }

    public void info(String message) {
        logger.info(message);
        plugin.getLogger().info(message);
    }

    public void warn(String message) {
        logger.warn(message);
        plugin.getLogger().warning(message);
    }

    public void error(String message) {
        logger.error(message);
        plugin.getLogger().severe(message);
    }

    public void fatal(String message) {
        logger.fatal(message);
        plugin.getLogger().severe("[FATAL] " + message);
    }
}
