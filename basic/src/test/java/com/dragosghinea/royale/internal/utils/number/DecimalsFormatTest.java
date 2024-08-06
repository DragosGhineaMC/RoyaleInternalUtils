package com.dragosghinea.royale.internal.utils.number;

import com.dragosghinea.royale.internal.utils.number.impl.DecimalsRoyaleNumberFormat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecimalsFormatTest {

    @Test
    @DisplayName("Numbers to string format")
    public void testToFormat() {
        DecimalsRoyaleNumberFormat decimalsRoyaleNumberFormat = new DecimalsRoyaleNumberFormat(2, false);
        assertEquals("100", decimalsRoyaleNumberFormat.toFormat(BigDecimal.valueOf(100)));
        assertEquals("100", decimalsRoyaleNumberFormat.toFormat(BigDecimal.valueOf(100.0)));
        assertEquals("100", decimalsRoyaleNumberFormat.toFormat(BigDecimal.valueOf(100.00003)));
        assertEquals("100", decimalsRoyaleNumberFormat.toFormat(BigDecimal.valueOf(100.005)));
        assertEquals("100.01", decimalsRoyaleNumberFormat.toFormat(BigDecimal.valueOf(100.006)));
        assertEquals("325.5", decimalsRoyaleNumberFormat.toFormat(BigDecimal.valueOf(325.5)));
        assertEquals("333.25", decimalsRoyaleNumberFormat.toFormat(BigDecimal.valueOf(333.250312)));
        assertEquals("1,000", decimalsRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1000)));
        assertEquals("1,000.5", decimalsRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1000.5)));
        assertEquals("3,000,000.25", decimalsRoyaleNumberFormat.toFormat(BigDecimal.valueOf(3000000.250312)));
    }

    @Test
    @DisplayName("String to number format")
    public void testFromFormat() {
        DecimalsRoyaleNumberFormat decimalsRoyaleNumberFormat = new DecimalsRoyaleNumberFormat(2, false);
        assertEquals(BigDecimal.valueOf(100), decimalsRoyaleNumberFormat.fromFormat("100"));
        assertEquals(BigDecimal.valueOf(100), decimalsRoyaleNumberFormat.fromFormat("100."));
        assertEquals(BigDecimal.valueOf(100.0), decimalsRoyaleNumberFormat.fromFormat("100.0"));
        assertEquals(BigDecimal.valueOf(100.01), decimalsRoyaleNumberFormat.fromFormat("100.01"));
        assertEquals(BigDecimal.valueOf(325.5), decimalsRoyaleNumberFormat.fromFormat("325.5"));
        assertEquals(BigDecimal.valueOf(333.25), decimalsRoyaleNumberFormat.fromFormat("333.25"));
        assertEquals(BigDecimal.valueOf(1000), decimalsRoyaleNumberFormat.fromFormat("1,000"));
        assertEquals(BigDecimal.valueOf(1000.5), decimalsRoyaleNumberFormat.fromFormat("1,000.5"));
        assertEquals(BigDecimal.valueOf(3000000.25), decimalsRoyaleNumberFormat.fromFormat("3,000,000.25"));
    }
}
