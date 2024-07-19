package com.dragosghinea.royale.internal.utils.messages;

import org.bukkit.command.CommandSender;

public interface MessageSender {

    void sendMessage(CommandSender toSendTo, String message);

}
