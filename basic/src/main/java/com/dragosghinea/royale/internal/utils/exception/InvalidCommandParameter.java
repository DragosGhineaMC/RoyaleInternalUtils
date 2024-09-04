package com.dragosghinea.royale.internal.utils.exception;

import lombok.Getter;

@Getter
public class InvalidCommandParameter extends Exception {

    private final int parameterIndex;
    private final Class<?> parameterType;

    public InvalidCommandParameter(int parameterIndex, Class<?> parameterType) {
        super("Invalid command parameter at index " + parameterIndex);
        this.parameterIndex = parameterIndex;
        this.parameterType = parameterType;
    }

    public InvalidCommandParameter(int parameterIndex, Class<?> parameterType, Throwable cause) {
        super("Invalid command parameter at index " + parameterIndex, cause);
        this.parameterIndex = parameterIndex;
        this.parameterType = parameterType;
    }
}
