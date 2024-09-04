package com.dragosghinea.royale.internal.utils.command.parameters.mapper;

import com.dragosghinea.royale.internal.utils.exception.InvalidCommandParameter;

import java.util.Stack;

public interface MapFromStack<T> {

    T map(Stack<String> args) throws InvalidCommandParameter;

}
