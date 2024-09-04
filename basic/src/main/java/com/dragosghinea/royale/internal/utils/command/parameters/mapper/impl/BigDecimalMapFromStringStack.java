package com.dragosghinea.royale.internal.utils.command.parameters.mapper.impl;

import com.dragosghinea.royale.internal.utils.command.parameters.mapper.MapFromStack;
import com.dragosghinea.royale.internal.utils.exception.InvalidCommandParameter;
import com.dragosghinea.royale.internal.utils.number.RoyaleNumberFormat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Stack;

public class BigDecimalMapFromStringStack implements MapFromStack<BigDecimal> {

    private final List<RoyaleNumberFormat> numberFormats;

    public BigDecimalMapFromStringStack(List<RoyaleNumberFormat> numberFormats) {
        this.numberFormats = numberFormats;
    }

    @Override
    public BigDecimal map(Stack<String> args) throws InvalidCommandParameter {
        String toConvert = args.pop();

        for (RoyaleNumberFormat numberFormat : numberFormats) {
            try {
                return numberFormat.fromFormat(toConvert);
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        throw new InvalidCommandParameter(0, BigDecimal.class);
    }
}
