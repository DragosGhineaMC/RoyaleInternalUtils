package com.dragosghinea.royale.internal.utils.logger;


import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class RoyaleLogger {
    private final FileLoggerWithRotation logger;
    private final Plugin plugin;
    private final Level level;

    public RoyaleLogger(Plugin plugin, String fileName) {
        this(plugin, fileName, Level.INFO);
    }

    public RoyaleLogger(Plugin plugin, String fileName, Level level) {
        this.plugin = plugin;
        this.level = level;
        try {
            this.logger = new FileLoggerWithRotation(new File(plugin.getDataFolder(), String.join(File.pathSeparator, "logs", fileName)), fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create logger", e);
        }
    }

    public void debug(String message) {
        if (!level.isEnabled(Level.DEBUG))
            return;

        logger.log(Level.DEBUG, message);
        plugin.getLogger().info("[DEBUG] " + message);
    }

    public void info(String message) {
        if (!level.isEnabled(Level.INFO))
            return;

        logger.log(Level.INFO, message);
        plugin.getLogger().info(message);
    }

    public void warn(String message) {
        if (!level.isEnabled(Level.WARN))
            return;

        logger.log(Level.WARN, message);
        plugin.getLogger().warning(message);
    }

    public void error(String message) {
        if (!level.isEnabled(Level.ERROR))
            return;

        logger.log(Level.ERROR, message);
        plugin.getLogger().severe(message);
    }

    public void fatal(String message) {
        if (!level.isEnabled(Level.FATAL))
            return;

        logger.log(Level.FATAL, message);
        plugin.getLogger().severe("[FATAL] " + message);
    }
}
