package com.dragosghinea.royale.internal.utils.command.parameters.mapper.impl;

import com.dragosghinea.royale.internal.utils.command.parameters.mapper.MapFromStack;
import com.dragosghinea.royale.internal.utils.exception.InvalidCommandParameter;

import java.util.Stack;

public class IntegerMapFromStringStack implements MapFromStack<Integer> {
    @Override
    public Integer map(Stack<String> args) throws InvalidCommandParameter {
        String toConvert = args.pop();

        try {
            return Integer.parseInt(toConvert);
        } catch (NumberFormatException e) {
            throw new InvalidCommandParameter(0, Integer.class, e);
        }
    }
}
