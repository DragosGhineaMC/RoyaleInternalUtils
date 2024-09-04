package com.dragosghinea.royale.internal.utils.command.parameters;

import com.dragosghinea.royale.internal.utils.exception.InvalidCommandParameter;

import java.util.Stack;

public interface ResolveMethodParameters {

    Object[] processParametersFromString(Stack<String> args, Class<?>... parameterTypes) throws InvalidCommandParameter;
}
