package com.dragosghinea.royale.internal.utils.command;

import lombok.Getter;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

@Getter
public class RoyaleBaseCommand extends Command implements TabExecutor, PluginIdentifiableCommand {
    private final Plugin plugin;
    protected final Map<String, CmdRoute> routes;

    public RoyaleBaseCommand(Plugin plugin, String name, String permission, String permissionMessage, List<String> aliases, Map<String, CmdRoute> routes) {
        this(plugin, name, aliases, routes);
        super.setPermission(permission);
        super.setPermissionMessage(permissionMessage);
    }

    public RoyaleBaseCommand(Plugin plugin, String name, List<String> aliases, Map<String, CmdRoute> routes) {
        super(name);
        if (aliases != null)
            super.setAliases(aliases);
        this.plugin = plugin;
        this.routes = routes;
    }

    private CmdRoute getDeepestRoute(CommandSender sender, Stack<String> args) {
        if (routes.isEmpty())
            throw new IllegalStateException("No routes defined for command " + getName());

        CmdRoute route = null;
        Map<String, CmdRoute> routesToCheck = routes;
        while (true) {
            String routeToCheck = args.isEmpty() ? "default" : args.pop();

            if (routesToCheck.containsKey(routeToCheck)) {
                route = routes.get(routeToCheck);
                if (!route.canExecute(sender, false))
                    break;

                routesToCheck = route.subRoutes();
                continue;
            }

            break;
        }

        return route;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Stack<String> argStack = new Stack<>();
        int i = args.length - 1;
        while (i >= 0) {
            argStack.push(args[i]);
            i--;
        }

        CmdRoute route = getDeepestRoute(commandSender, argStack);
        if (route == null) {
            routes.values().stream()
                    .filter(cmdRoute -> cmdRoute.canExecute(commandSender, true))
                    .map(routeCmd -> routeCmd.getDescription().isEmpty() ?
                            String.format("§c/%s %s", s, routeCmd.getName())
                            :
                            String.format("§c/%s %s §7- §f%s", s, routeCmd.getName(), routeCmd.getDescription())
                    )
                    .forEach(commandSender::sendMessage);

            return false;
        }

        return route.execute(commandSender, argStack);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        Stack<String> argStack = new Stack<>();
        int i = args.length - 1;
        while (i >= 0) {
            argStack.push(args[i]);
            i--;
        }

        CmdRoute route = getDeepestRoute(commandSender, argStack);
        if (route == null) {
            return routes.values().stream()
                    .filter(cmdRoute -> cmdRoute.canExecute(commandSender, true))
                    .map(CmdRoute::getName)
                    .collect(Collectors.toList());
        }

        return route.tabComplete(commandSender, argStack);
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
