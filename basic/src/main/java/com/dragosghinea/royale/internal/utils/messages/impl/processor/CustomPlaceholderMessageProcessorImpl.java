package com.dragosghinea.royale.internal.utils.messages.impl.processor;

import com.dragosghinea.royale.internal.utils.messages.MessageProcessor;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public class CustomPlaceholderMessageProcessorImpl implements MessageProcessor {

    private final Map<String, Function<CommandSender, String>> placeholdersCompute;

    @Override
    public String processMessage(CommandSender toProcessFor, String message) {
        for (Map.Entry<String, Function<CommandSender, String>> entry : placeholdersCompute.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue().apply(toProcessFor));
        }

        return message;
    }

}
