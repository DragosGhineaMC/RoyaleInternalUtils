package com.dragosghinea.royale.internal.utils.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

@RequiredArgsConstructor
@Getter
public class CmdContext {

    private final CommandSender sender;
    private final RoyaleBaseCommand command;
    private final List<CmdRoute> passedThrough = new LinkedList<>();
    private final Stack<String> argsLeftToProcess;
}
