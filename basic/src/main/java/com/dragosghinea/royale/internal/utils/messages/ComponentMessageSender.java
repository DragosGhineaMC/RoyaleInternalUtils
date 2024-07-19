package com.dragosghinea.royale.internal.utils.messages;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

public interface ComponentMessageSender {

    void sendMessage(CommandSender toSendTo, BaseComponent[] component);

}
