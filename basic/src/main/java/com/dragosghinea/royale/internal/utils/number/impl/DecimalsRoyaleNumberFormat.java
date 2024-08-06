package com.dragosghinea.royale.internal.utils.number.impl;

import com.dragosghinea.royale.internal.utils.number.RoyaleNumberFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DecimalsRoyaleNumberFormat implements RoyaleNumberFormat {

    public static void main(String[] args) {
        DecimalsRoyaleNumberFormat decimalsRoyaleNumberFormat = new DecimalsRoyaleNumberFormat(3, false);
        System.out.println(decimalsRoyaleNumberFormat.toFormat(new BigDecimal("123.456")));
        System.out.println(decimalsRoyaleNumberFormat.toFormat(new BigDecimal("123.2")));
    }

    private final DecimalFormat decimalFormat;

    public DecimalsRoyaleNumberFormat(int numberOfDecimals, boolean forceAllDecimals) {
        decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingSize(3);
        decimalFormat.setGroupingUsed(true);

        if (forceAllDecimals)
            decimalFormat.setMinimumFractionDigits(numberOfDecimals);
        decimalFormat.setMaximumFractionDigits(numberOfDecimals);
    }

    @Override
    public String toFormat(BigDecimal number) {
        return decimalFormat.format(number);
    }

    @Override
    public BigDecimal fromFormat(String number) {
        number = number.replace(decimalFormat.getDecimalFormatSymbols().getGroupingSeparator() + "", "");
        return new BigDecimal(number).setScale(decimalFormat.getMaximumFractionDigits(), RoundingMode.HALF_EVEN);
    }
}
