package com.dragosghinea.royale.internal.utils.command;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface CmdRoute {

    String getName();

    default String getDescription() {
        return "";
    }

    boolean canExecute(CmdContext context, boolean silentCheck);

    boolean execute(CmdContext context);

    default List<String> tabComplete(CmdContext context) {
        return subRoutes().values().stream()
                .filter(cmdRoute -> cmdRoute.canExecute(context, true))
                .map(CmdRoute::getName)
                .collect(Collectors.toList());
    }

    default Map<String, CmdRoute> subRoutes() {
        return Collections.emptyMap();
    }
}
