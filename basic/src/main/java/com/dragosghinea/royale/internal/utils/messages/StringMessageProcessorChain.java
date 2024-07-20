package com.dragosghinea.royale.internal.utils.messages;

import org.bukkit.command.CommandSender;

public interface StringMessageProcessorChain {

    String processMessage(CommandSender commandSender, String message);

    void addProcessor(MessageProcessor processor);

    void removeProcessor(MessageProcessor processor);

    void bringProcessorToFront(MessageProcessor processor);

    void sendProcessToBack(MessageProcessor processor);

}
