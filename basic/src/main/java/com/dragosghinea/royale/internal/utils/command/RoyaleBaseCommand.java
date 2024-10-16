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

    public String getColor() {
        return "§c";
    }

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

    private CmdContext generateContext(CommandSender sender, String[] args) {
        if (routes.isEmpty())
            throw new IllegalStateException("No routes defined for command " + getName());

        Stack<String> argStack = new Stack<>();
        int i = args.length - 1;
        while (i >= 0) {
            argStack.push(args[i]);
            i--;
        }

        CmdContext context = new CmdContext(sender, this, argStack);
        CmdRoute route;
        Map<String, CmdRoute> routesToCheck = routes;
        while (true) {
            String routeToCheck = argStack.isEmpty() ? "" : argStack.peek();

            if (routesToCheck.containsKey(routeToCheck)) {
                if (!argStack.isEmpty())
                    argStack.pop();

                route = routesToCheck.get(routeToCheck);
                if (!route.canExecute(context, false))
                    break;

                routesToCheck = route.subRoutes();
                context.getPassedThrough().add(route);
                continue;
            }

            break;
        }

        return context;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        CmdContext context = generateContext(commandSender, args);

        if (context.getPassedThrough().isEmpty()) {
            routes.values().stream()
                    .filter(cmdRoute -> cmdRoute.canExecute(context, true))
                    .map(routeCmd -> routeCmd.getDescription().isEmpty() ?
                            String.format("%s/%s %s", getColor(), s, routeCmd.getName())
                            :
                            String.format("%s/%s %s §7- §f%s", getColor(), s, routeCmd.getName(), routeCmd.getDescription())
                    )
                    .forEach(commandSender::sendMessage);

            return false;
        }

        CmdRoute route = context.getPassedThrough().remove(context.getPassedThrough().size() - 1);
        return route.execute(context);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        CmdContext context = generateContext(commandSender, args);
        if (context.getPassedThrough().isEmpty()) {
            return routes.values().stream()
                    .filter(cmdRoute -> cmdRoute.canExecute(context, true))
                    .map(CmdRoute::getName)
                    .collect(Collectors.toList());
        }

        CmdRoute route = context.getPassedThrough().remove(context.getPassedThrough().size() - 1);
        return route.tabComplete(context);
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
