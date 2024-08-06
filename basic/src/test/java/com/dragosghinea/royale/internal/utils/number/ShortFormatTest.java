package com.dragosghinea.royale.internal.utils.number;

import com.dragosghinea.royale.internal.utils.number.impl.ShortRoyaleNumberFormat;
import com.dragosghinea.yaml.ConfigHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortFormatTest {

    private final static String SHORT_FORMATS =
            "short-formats:\n" +
            "  unit1:\n" +
            "    number-of-zeros: 3\n" +
            "    unit-name: 'K'\n" +
            "  unit2:\n" +
            "    number-of-zeros: 6\n" +
            "    unit-name: 'M'\n" +
            "  unit3:\n" +
            "    number-of-zeros: 9\n" +
            "    unit-name: 'G'\n" +
            "  unit4:\n" +
            "    number-of-zeros: 12\n" +
            "    unit-name: 'T'\n" +
            "  unit5:\n" +
            "    number-of-zeros: 15\n" +
            "    unit-name: 'Q'";

    private static ShortRoyaleNumberFormat shortRoyaleNumberFormat;

    @BeforeAll
    public static void setUp() throws IOException {
        File file = new File("short-format.yml");
        file.createNewFile();

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(SHORT_FORMATS);
        }

        ShortFormatCfg shortFormatCfg = new ConfigHandler<>(ShortFormatCfg.class, Paths.get("short-format.yml")).load();
        shortRoyaleNumberFormat = new ShortRoyaleNumberFormat(shortFormatCfg.getShortFormats().values().toArray(new ShortFormatPairCfg[0]));
    }

    @Test
    @DisplayName("Test numbers to format under first unit (1000)")
    public void testShortFormatBelowFirstUnit() {
        assertEquals("100", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(100)));
        assertEquals("325.5", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(325.5)));
        assertEquals("333.25", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(333.250312)));
        assertEquals("999", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(999)));
    }

    @Test
    @DisplayName("Test numbers to format above first unit (1000)")
    public void testShortFormatAboveFirstUnit() {
        assertEquals("1K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1000)));
        assertEquals("1.2K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1200.32)));
        assertEquals("1.23K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1234.56)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.5)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.999)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.0001)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.0005)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.0009)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.00001)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.00005)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.00009)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.000001)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.00000009)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.000000001)));
        assertEquals("1.5K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1500.000000005)));
        assertEquals("1.76K", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1759.999)));
        assertEquals("1.3M", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1_300_200.999)));
        assertEquals("1.29G", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1_290_320_000.999)));
        assertEquals("1.5T", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1_500_320_000_020.999)));
        assertEquals("1.7Q", shortRoyaleNumberFormat.toFormat(BigDecimal.valueOf(1_700_320_010_000_909.999)));
    }

    @Test
    @DisplayName("Test numbers from format under first unit (1000)")
    public void testFromShortFormatBelowFirstUnit() {
        assertEquals(BigDecimal.valueOf(100), shortRoyaleNumberFormat.fromFormat("100"));
        assertEquals(BigDecimal.valueOf(325.5), shortRoyaleNumberFormat.fromFormat("325.5"));
        assertEquals(BigDecimal.valueOf(333.25), shortRoyaleNumberFormat.fromFormat("333.25"));
        assertEquals(BigDecimal.valueOf(999), shortRoyaleNumberFormat.fromFormat("999."));
        assertEquals(0, BigDecimal.valueOf(999).compareTo(shortRoyaleNumberFormat.fromFormat("999.00")));
        assertEquals(0, BigDecimal.valueOf(999.0001).compareTo(shortRoyaleNumberFormat.fromFormat("999.0001")));
        assertEquals(BigDecimal.valueOf(999), shortRoyaleNumberFormat.fromFormat("999"));
    }

    @Test
    @DisplayName("Test numbers from format above first unit (1000)")
    public void testFromShortFormatAboveFirstUnit() {
        assertEquals(BigDecimal.valueOf(1000), shortRoyaleNumberFormat.fromFormat("1K"));
        assertEquals(BigDecimal.valueOf(1200.0), shortRoyaleNumberFormat.fromFormat("1.2K"));
        assertEquals(BigDecimal.valueOf(1230.0).setScale(2, RoundingMode.HALF_EVEN), shortRoyaleNumberFormat.fromFormat("1.23K"));
        assertEquals(BigDecimal.valueOf(1500.0), shortRoyaleNumberFormat.fromFormat("1.5K"));
        assertEquals(BigDecimal.valueOf(1760).setScale(2, RoundingMode.HALF_EVEN), shortRoyaleNumberFormat.fromFormat("1.76K"));
        assertEquals(BigDecimal.valueOf(1_300_000), shortRoyaleNumberFormat.fromFormat("1,300K"));
        assertEquals(BigDecimal.valueOf(1_300_000.0), shortRoyaleNumberFormat.fromFormat("1.3M"));
        assertEquals(BigDecimal.valueOf(1_290_000_000).setScale(2, RoundingMode.HALF_EVEN), shortRoyaleNumberFormat.fromFormat("1.29G"));
        assertEquals(0, BigDecimal.valueOf(1_500_000_000_000.0).compareTo(shortRoyaleNumberFormat.fromFormat("1.5T")));
        assertEquals(0, BigDecimal.valueOf(1_700_000_000_000_000.0).compareTo(shortRoyaleNumberFormat.fromFormat("1.7Q")));
    }

    @AfterAll
    public static void tearDown() {
        File file = new File("short-format.yml");
        file.delete();

        shortRoyaleNumberFormat = null;
    }
}
