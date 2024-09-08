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

    boolean execute(CmdContext context);

    default List<String> tabComplete(CmdContext context) {
        String currentInput = context.getArgsLeftToProcess().isEmpty() ? "" : context.getArgsLeftToProcess().lastElement();
        return subRoutes().values().stream()
                .filter(cmdRoute -> cmdRoute.canExecute(context, true))
                .map(CmdRoute::getName)
                .filter(routeName -> routeName.startsWith(currentInput))
                .toList();
    }

    default Map<String, CmdRoute> subRoutes() {
        return Collections.emptyMap();
    }
}
