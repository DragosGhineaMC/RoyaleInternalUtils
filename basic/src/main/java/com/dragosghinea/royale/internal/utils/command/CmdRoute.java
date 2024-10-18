package com.dragosghinea.royale.internal.utils.command;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface CmdRoute {

    String getName();

    default String getDescription() {
        return "";
    }

    boolean canExecute(CmdContext context, boolean silentCheck);

    default boolean execute(CmdContext context) {
        StringBuilder builder = new StringBuilder("/");
        builder.append(context.getCommand().getName());
        context.getPassedThrough().forEach(route -> builder.append(" ").append(route.getName()));
        builder.append(" ").append(getName());
        String prefix = builder.toString();

        subRoutes().values().stream()
                .filter(cmdRoute -> cmdRoute.canExecute(context, true))
                .map(routeCmd -> routeCmd.getDescription().isEmpty() ?
                        String.format("%s%s %s", context.getCommand().getColor(), prefix, routeCmd.getName())
                        :
                        String.format("%s%s %s §7- §f%s", context.getCommand().getColor(), prefix, routeCmd.getName(), routeCmd.getDescription())
                )
                .forEach(s -> context.getSender().sendMessage(s));

        return true;
    }

    default List<String> tabComplete(CmdContext context) {
        Map<String, CmdRoute> subRoutesToUse;
        if (context.getArgsLeftToProcess().isEmpty()) {
            if (context.getPassedThrough().isEmpty())
                subRoutesToUse = context.getCommand().getRoutes();
            else
                subRoutesToUse = context.getPassedThrough().getLast().subRoutes();
        } else {
            subRoutesToUse = subRoutes();
        }

        String currentInput = context.getArgsLeftToProcess().isEmpty() ? "" : context.getArgsLeftToProcess().lastElement();
        return subRoutesToUse.values().stream()
                .filter(cmdRoute -> cmdRoute.canExecute(context, true))
                .map(CmdRoute::getName)
                .filter(routeName -> routeName.startsWith(currentInput))
                .toList();
    }

    default Map<String, CmdRoute> subRoutes() {
        return Collections.emptyMap();
    }
}
