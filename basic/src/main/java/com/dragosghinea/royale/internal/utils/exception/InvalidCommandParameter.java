package com.dragosghinea.royale.internal.utils.exception;

import lombok.Getter;

@Getter
public class InvalidCommandParameter extends Exception {

    private final int parameterIndex;
    private final Class<?> parameterType;
    private final String argumentThatWasNotValid;

    public InvalidCommandParameter(int parameterIndex, Class<?> parameterType, String argumentThatWasNotValid) {
        super("Invalid command parameter at index " + parameterIndex);
        this.parameterIndex = parameterIndex;
        this.parameterType = parameterType;
        this.argumentThatWasNotValid = argumentThatWasNotValid;
    }

    public InvalidCommandParameter(int parameterIndex, Class<?> parameterType, String argumentThatWasNotValid, Throwable cause) {
        super("Invalid command parameter at index " + parameterIndex, cause);
        this.parameterIndex = parameterIndex;
        this.parameterType = parameterType;
        this.argumentThatWasNotValid = argumentThatWasNotValid;
    }
}
