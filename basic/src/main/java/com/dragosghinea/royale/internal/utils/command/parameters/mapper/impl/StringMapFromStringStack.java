package com.dragosghinea.royale.internal.utils.command.parameters.mapper.impl;

import com.dragosghinea.royale.internal.utils.command.parameters.mapper.MapFromStack;
import com.dragosghinea.royale.internal.utils.exception.InvalidCommandParameter;

import java.util.Stack;

public class StringMapFromStringStack implements MapFromStack<String> {
    @Override
    public String map(Stack<String> args) throws InvalidCommandParameter {
        return args.pop();
    }
}
