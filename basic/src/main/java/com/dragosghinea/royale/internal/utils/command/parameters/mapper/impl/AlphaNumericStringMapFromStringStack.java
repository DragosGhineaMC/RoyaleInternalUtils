package com.dragosghinea.royale.internal.utils.command.parameters.mapper.impl;

import com.dragosghinea.royale.internal.utils.command.parameters.mapper.MapFromStack;
import com.dragosghinea.royale.internal.utils.exception.InvalidCommandParameter;

import java.util.Stack;
import java.util.regex.Pattern;

public class AlphaNumericStringMapFromStringStack implements MapFromStack<String> {
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]*$");

    @Override
    public String map(Stack<String> args) throws InvalidCommandParameter {
        String arg = args.pop();
        if (!ALPHANUMERIC_PATTERN.matcher(arg).matches()) {
            throw new InvalidCommandParameter(0, String.class, "Invalid alphanumeric string: '" + arg+"'");
        }

        return arg;
    }
}
