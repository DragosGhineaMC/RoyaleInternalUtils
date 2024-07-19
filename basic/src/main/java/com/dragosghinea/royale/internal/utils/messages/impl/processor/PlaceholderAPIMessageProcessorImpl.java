package com.dragosghinea.royale.internal.utils.messages.impl.processor;

import com.dragosghinea.royale.internal.utils.messages.MessageProcessor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaceholderAPIMessageProcessorImpl implements MessageProcessor {

    @Override
    public String processMessage(CommandSender toProcessFor, String message) {
        Player player = toProcessFor instanceof Player ? (Player) toProcessFor : null;
        return PlaceholderAPI.setPlaceholders(player, message);
    }
}
