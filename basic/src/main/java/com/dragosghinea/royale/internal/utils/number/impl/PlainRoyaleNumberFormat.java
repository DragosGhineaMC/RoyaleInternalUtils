package com.dragosghinea.royale.internal.utils.number.impl;

import com.dragosghinea.royale.internal.utils.number.RoyaleNumberFormat;

import java.math.BigDecimal;

public class PlainRoyaleNumberFormat implements RoyaleNumberFormat {

    @Override
    public String toFormat(BigDecimal number) {
        return number.toPlainString();
    }

    @Override
    public BigDecimal fromFormat(String number) {
        return new BigDecimal(number);
    }
}
