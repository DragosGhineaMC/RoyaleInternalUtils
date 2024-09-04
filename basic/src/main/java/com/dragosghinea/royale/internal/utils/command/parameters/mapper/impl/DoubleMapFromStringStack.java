package com.dragosghinea.royale.internal.utils.command.parameters.mapper.impl;

import com.dragosghinea.royale.internal.utils.command.parameters.mapper.MapFromStack;
import com.dragosghinea.royale.internal.utils.exception.InvalidCommandParameter;

import java.util.Stack;

public class DoubleMapFromStringStack implements MapFromStack<Double> {
    @Override
    public Double map(Stack<String> args) throws InvalidCommandParameter {
        String toConvert = args.pop();

        try {
            return Double.parseDouble(toConvert);
        } catch (NumberFormatException e) {
            throw new InvalidCommandParameter(0, Double.class, e);
        }
    }
}
