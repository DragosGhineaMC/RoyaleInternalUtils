package com.dragosghinea.royale.internal.utils.command;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoyaleCommand extends Command implements TabExecutor, PluginIdentifiableCommand {
    private final Plugin plugin;
    protected final Map<String, CommandRoute> routes;

    public RoyaleCommand(Plugin plugin, String name, String permission, String permissionMessage, List<String> aliases, Map<String, CommandRoute> routes) {
        this(plugin, name, aliases, routes);
        super.setPermission(permission);
        super.setPermissionMessage(permissionMessage);
    }

    public RoyaleCommand(Plugin plugin, String name, List<String> aliases, Map<String, CommandRoute> routes) {
        super(name);
        super.setAliases(aliases);
        this.plugin = plugin;
        this.routes = routes;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {
            CommandRoute fallback = routes.getOrDefault("", null);
            if (fallback == null) {
                commandSender.sendMessage("§cNot enough arguments!");
                return false;
            }

            return fallback.onCommand(commandSender, args);
        }

        CommandRoute route = routes.getOrDefault(args[0], null);
        if (route == null) {
            commandSender.sendMessage("§cInvalid argument!");
            return false;
        }

        return route.onCommand(commandSender, args);
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> toReturn = Collections.emptyList();
        String arg = args[0];

        if (args.length == 1)
            return routes.entrySet().stream()
                    .filter(commandRouteEntry -> commandRouteEntry.getKey().startsWith(arg))
                    .filter(commandRouteEntry -> "none".equals(commandRouteEntry.getValue().getPermission()) || commandSender.hasPermission(commandRouteEntry.getValue().getPermission()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());


        final CommandRoute route = routes.getOrDefault(arg, null);

        if (route != null && ("none".equals(route.getPermission()) || commandSender.hasPermission(route.getPermission()))) {
            toReturn = route.onTabComplete(commandSender, args);
        }

        return toReturn;
    }

    @Override
    public boolean execute(CommandSender commandSender, String commandLabel, String[] args) {
        if (getPermission() != null && !commandSender.hasPermission(getPermission())) {
            commandSender.sendMessage(super.getPermissionMessage());
            return false;
        }

        return this.onCommand(commandSender, this, commandLabel, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (getPermission() != null && !sender.hasPermission(getPermission())) {
            return Collections.emptyList();
        }

        return this.onTabComplete(sender, this, alias, args);
    }
}
