package com.dragosghinea.royale.internal.utils.number.impl;

import com.dragosghinea.royale.internal.utils.number.RoyaleNumberFormat;
import com.dragosghinea.royale.internal.utils.number.ShortFormatPairCfg;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ShortRoyaleNumberFormat implements RoyaleNumberFormat {

    private final NavigableMap<BigDecimal, String> suffixes = new TreeMap<>();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");


    public ShortRoyaleNumberFormat(ShortFormatPairCfg... shortFormats) {
        for (ShortFormatPairCfg shortFormat : shortFormats) {
            suffixes.put(BigDecimal.TEN.pow(shortFormat.getNumberOfZeros()), shortFormat.getUnitName());
        }
    }

    @Override
    public String toFormat(BigDecimal number) {
        if (number.compareTo(BigDecimal.ZERO) < 0) return "-" + toFormat(number.negate());

        if (suffixes.firstEntry() == null) return decimalFormat.format(number);

        BigDecimal minimum = suffixes.firstEntry().getKey();
        if (number.compareTo(minimum) < 0) return decimalFormat.format(number);

        Map.Entry<BigDecimal, String> suffix = suffixes.floorEntry(number);
        BigDecimal truncated = number.divide(suffix.getKey(), RoundingMode.HALF_EVEN);

        return decimalFormat.format(truncated) + suffix.getValue();
    }

    @Override
    public BigDecimal fromFormat(String number) {
        number = number.replace(decimalFormat.getDecimalFormatSymbols().getGroupingSeparator() + "", "");

        Map.Entry<BigDecimal, String> foundEntry = null;
        for (Map.Entry<BigDecimal, String> entry : suffixes.entrySet()) {
            String unit = entry.getValue();
            if (number.toUpperCase().endsWith(unit.toUpperCase())) {
                foundEntry = entry;
                break;
            }
        }

        if (foundEntry == null) return new BigDecimal(number);

        return new BigDecimal(number
                .replace(foundEntry.getValue().toUpperCase(), "")
                .replace(foundEntry.getValue().toLowerCase(), "")
        ).multiply(foundEntry.getKey());
    }
}
