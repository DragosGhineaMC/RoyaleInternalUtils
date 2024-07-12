package com.dragosghinea.royale.internal.utils.command;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface CommandRoute {

    default boolean onCommand(CommandSender sender, String[] args) {
        return onCommand(sender, args, 0);
    }

    default Map<String, CommandRoute> getChildrenRoutes() {
        return null;
    }

    default String getPermission() {
        return "none";
    }

    default String getPermissionMessage() {
        return "§cYou do not have permission to use this command!";
    }

    default boolean onCommand(CommandSender sender, String[] args, int depth) {
        Map<String, CommandRoute> routes = getChildrenRoutes();
        if (routes == null)
            throw new RuntimeException("No child routes specified and onCommand not implemented!");

        if (args.length - depth == 0) {
            CommandRoute fallback = routes.getOrDefault("", null);
            if (fallback == null) {
                sender.sendMessage("§cNot enough arguments!");
                return false;
            }

            return fallback.onCommand(sender, args);
        }

        CommandRoute route = routes.getOrDefault(args[depth], null);
        if (route == null) {
            sender.sendMessage("§cInvalid argument!");
            return false;
        }

        if (!"none".equals(route.getPermission()) && !sender.hasPermission(route.getPermission())) {
            sender.sendMessage(route.getPermissionMessage());
            return false;
        }

        return route.onCommand(sender, args);
    }

    default List<String> onTabComplete(CommandSender sender, String[] args) {
        return onTabComplete(sender, args, 0);
    }

    default List<String> onTabComplete(CommandSender sender, String[] args, int depth) {
        Map<String, CommandRoute> childrenRoutes = getChildrenRoutes();
        if (childrenRoutes == null)
            return Collections.emptyList();

        List<String> toReturn = Collections.emptyList();
        String arg = args[depth];

        if (args.length - depth == 1)
            return childrenRoutes.entrySet().stream()
                    .filter(commandRouteEntry -> commandRouteEntry.getKey().startsWith(arg))
                    .filter(commandRouteEntry -> "none".equals(commandRouteEntry.getValue().getPermission()) || sender.hasPermission(commandRouteEntry.getValue().getPermission()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());


        final CommandRoute route = childrenRoutes.getOrDefault(arg, null);

        if (route != null && ("none".equals(route.getPermission()) || sender.hasPermission(route.getPermission()))) {
            toReturn = route.onTabComplete(sender, args, depth+1);
        }

        return toReturn;
    }
}
