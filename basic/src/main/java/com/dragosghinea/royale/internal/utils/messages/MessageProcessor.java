package com.dragosghinea.royale.internal.utils.messages;

import org.bukkit.command.CommandSender;

public interface MessageProcessor {

    String processMessage(CommandSender toProcessFor, String message);

}
