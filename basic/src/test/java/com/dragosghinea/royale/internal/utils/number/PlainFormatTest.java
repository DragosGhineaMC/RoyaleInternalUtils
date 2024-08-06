package com.dragosghinea.royale.internal.utils.number;

import com.dragosghinea.royale.internal.utils.number.impl.DecimalsRoyaleNumberFormat;
import com.dragosghinea.royale.internal.utils.number.impl.PlainRoyaleNumberFormat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlainFormatTest {

    @Test
    @DisplayName("Numbers to string format")
    public void testToFormat() {
        PlainRoyaleNumberFormat plainRoyaleNumberFormat = new PlainRoyaleNumberFormat();
        assertEquals("100", plainRoyaleNumberFormat.toFormat(BigDecimal.valueOf(100)));
        assertEquals("100.0", plainRoyaleNumberFormat.toFormat(BigDecimal.valueOf(100.0)));
        assertEquals("100.00003", plainRoyaleNumberFormat.toFormat(BigDecimal.valueOf(100.00003)));
        assertEquals("100.005", plainRoyaleNumberFormat.toFormat(BigDecimal.valueOf(100.005)));
        assertEquals("100.006", plainRoyaleNumberFormat.toFormat(BigDecimal.valueOf(100.006)));
        assertEquals("325.5", plainRoyaleNumberFormat.toFormat(BigDecimal.valueOf(325.5)));
        assertEquals("333.250312", plainRoyaleNumberFormat.toFormat(BigDecimal.valueOf(333.250312)));
        assertEquals("1000", plainRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1000)));
        assertEquals("1000.5", plainRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1000.5)));
        assertEquals("3000000.25", plainRoyaleNumberFormat.toFormat(BigDecimal.valueOf(3000000.25)));
    }

    @Test
    @DisplayName("String to number format")
    public void testFromFormat() {
        PlainRoyaleNumberFormat plainRoyaleNumberFormat = new PlainRoyaleNumberFormat();
        assertEquals(BigDecimal.valueOf(100), plainRoyaleNumberFormat.fromFormat("100"));
        assertEquals(BigDecimal.valueOf(100), plainRoyaleNumberFormat.fromFormat("100."));
        assertEquals(BigDecimal.valueOf(100.0), plainRoyaleNumberFormat.fromFormat("100.0"));
        assertEquals(BigDecimal.valueOf(100.01), plainRoyaleNumberFormat.fromFormat("100.01"));
        assertEquals(BigDecimal.valueOf(325.5), plainRoyaleNumberFormat.fromFormat("325.5"));
        assertEquals(BigDecimal.valueOf(333.25), plainRoyaleNumberFormat.fromFormat("333.25"));
        assertEquals(BigDecimal.valueOf(1000), plainRoyaleNumberFormat.fromFormat("1000"));
        assertEquals(BigDecimal.valueOf(1000.5), plainRoyaleNumberFormat.fromFormat("1000.5"));
        assertEquals(BigDecimal.valueOf(3000000.25), plainRoyaleNumberFormat.fromFormat("3000000.25"));
    }

}
