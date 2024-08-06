package com.dragosghinea.royale.internal.utils.number;

import java.math.BigDecimal;

public interface RoyaleNumberFormat {

    String toFormat(BigDecimal number);

    BigDecimal fromFormat(String number);
}
