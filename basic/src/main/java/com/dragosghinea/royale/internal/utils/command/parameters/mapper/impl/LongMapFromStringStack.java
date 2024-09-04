package com.dragosghinea.royale.internal.utils.command.parameters.mapper.impl;

import com.dragosghinea.royale.internal.utils.command.parameters.mapper.MapFromStack;
import com.dragosghinea.royale.internal.utils.exception.InvalidCommandParameter;

import java.util.Stack;

public class LongMapFromStringStack implements MapFromStack<Long> {
    @Override
    public Long map(Stack<String> args) throws InvalidCommandParameter {
        String toConvert = args.pop();

        try {
            return Long.parseLong(toConvert);
        } catch (NumberFormatException e) {
            throw new InvalidCommandParameter(0, Long.class, e);
        }
    }
}
