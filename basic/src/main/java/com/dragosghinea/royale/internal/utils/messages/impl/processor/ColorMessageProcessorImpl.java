package com.dragosghinea.royale.internal.utils.messages.impl.processor;

import com.dragosghinea.royale.internal.utils.messages.MessageProcessor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ColorMessageProcessorImpl implements MessageProcessor {

    private static String applyColor(String message) {
        if (message == null)
            return ChatColor.translateAlternateColorCodes('&', "&cMissing Text");

        Matcher hexColorMatcher = Pattern.compile("\\{#([0-9A-Fa-f]{6})}").matcher(message);
        if (!hexColorMatcher.find())
            return ChatColor.translateAlternateColorCodes('&', message);

        do {
            String hexColorUnformatted = hexColorMatcher.group(0); // entire match {#RRGGBB}
            String hexColorCode = hexColorMatcher.group(1); // only RRGGBB part
            String hexColorCompiled = "§x" + Arrays.stream(hexColorCode.split(""))
                    .map((hexChar) -> "§" + hexChar)
                    .collect(Collectors.joining());

            message = message.replace(hexColorUnformatted, hexColorCompiled);
        } while (hexColorMatcher.find());

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public String processMessage(CommandSender toProcessFor, String message) {
        return applyColor(message);
    }
}
