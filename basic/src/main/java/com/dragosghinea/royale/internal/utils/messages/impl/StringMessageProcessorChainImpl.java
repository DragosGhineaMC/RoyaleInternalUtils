package com.dragosghinea.royale.internal.utils.messages.impl;

import com.dragosghinea.royale.internal.utils.messages.MessageProcessor;
import com.dragosghinea.royale.internal.utils.messages.StringMessageProcessorChain;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class StringMessageProcessorChainImpl implements StringMessageProcessorChain {

    private final List<MessageProcessor> processors = new ArrayList<>();

    @Override
    public String processMessage(CommandSender commandSender, String message) {
        for (MessageProcessor processor : processors) {
            message = processor.processMessage(commandSender, message);
        }

        return message;
    }

    @Override
    public void addProcessor(MessageProcessor processor) {
        processors.add(processor);
    }

    @Override
    public void removeProcessor(MessageProcessor processor) {
        processors.remove(processor);
    }

    @Override
    public void bringProcessorToFront(MessageProcessor processor) {
        processors.remove(processor);
        processors.add(0, processor);
    }

    @Override
    public void sendProcessToBack(MessageProcessor processor) {
        processors.remove(processor);
        processors.add(processor);
    }
}
