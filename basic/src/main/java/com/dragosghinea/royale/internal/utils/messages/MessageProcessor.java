package com.dragosghinea.royale.internal.utils.messages;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface MessageProcessor {

    String processMessage(CommandSender toProcessFor, String message);

}
