package com.dragosghinea.royale.internal.utils.command;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public interface CmdRoute {

    String getName();

    default String getDescription() {
        return "";
    }

    boolean canExecute(CommandSender sender, boolean silentCheck);

    boolean execute(CommandSender sender, Stack<String> args);

    default List<String> tabComplete(CommandSender sender, Stack<String> args) {
        return subRoutes().values().stream()
                .filter(cmdRoute -> cmdRoute.canExecute(sender, true))
                .map(CmdRoute::getName)
                .collect(Collectors.toList());
    }

    default Map<String, CmdRoute> subRoutes() {
        return Collections.emptyMap();
    }
}
