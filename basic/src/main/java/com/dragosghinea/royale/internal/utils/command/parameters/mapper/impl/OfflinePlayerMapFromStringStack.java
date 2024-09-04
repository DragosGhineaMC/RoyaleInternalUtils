package com.dragosghinea.royale.internal.utils.command.parameters.mapper.impl;

import com.dragosghinea.royale.internal.utils.command.parameters.mapper.MapFromStack;
import com.dragosghinea.royale.internal.utils.exception.InvalidCommandParameter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Stack;
import java.util.UUID;

public class OfflinePlayerMapFromStringStack implements MapFromStack<OfflinePlayer> {

    @Override
    public OfflinePlayer map(Stack<String> args) throws InvalidCommandParameter {
        String toConvert = args.pop();

        try {
            try {
                return Bukkit.getOfflinePlayer(UUID.fromString(toConvert));
            } catch (IllegalArgumentException e) {
                return Bukkit.getOfflinePlayer(toConvert);
            }
        } catch (Exception e) {
            throw new InvalidCommandParameter(0, OfflinePlayer.class, e);
        }
    }
}
