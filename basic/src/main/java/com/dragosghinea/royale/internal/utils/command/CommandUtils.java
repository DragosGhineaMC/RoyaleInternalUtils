package com.dragosghinea.royale.internal.utils.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandUtils {

    private final Plugin plugin;
    private final Map<String, RoyaleCommand> loadedCommands = new ConcurrentHashMap<>();

    private CommandMap commandMap = null;
    private CommandMap getCommandMap() {
        if (commandMap != null)
            return commandMap;

        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);

                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return commandMap;
    }

    public CommandUtils(Plugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unchecked")
    public void unregisterCommands() {
        try {
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            CommandMap commandMap = getCommandMap();
            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
            for (String commandName : loadedCommands.keySet()) {
                Command command = commandMap.getCommand(commandName);
                if (command == null)
                    continue;

                for (String alias : command.getAliases())
                    knownCommands.remove(alias);
                knownCommands.remove(command.getName());
                command.unregister(commandMap);
            }
            knownCommandsField.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadedCommands.clear();
    }

    public void registerCommand(RoyaleCommand pluginCommand) {
        loadedCommands.put(pluginCommand.getName(), pluginCommand);
        getCommandMap().register(plugin.getDescription().getName(), pluginCommand);
    }

    public void unregisterCommand(RoyaleCommand pluginCommand) {
        loadedCommands.remove(pluginCommand.getName());
        pluginCommand.unregister(getCommandMap());
    }

}
