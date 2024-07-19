package com.dragosghinea.royale.internal.utils.messages.impl.sender;

import com.dragosghinea.royale.internal.utils.messages.MessageSender;
import org.bukkit.command.CommandSender;

public class PlainMessageSenderImpl implements MessageSender {

    @Override
    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(message);
    }
}
