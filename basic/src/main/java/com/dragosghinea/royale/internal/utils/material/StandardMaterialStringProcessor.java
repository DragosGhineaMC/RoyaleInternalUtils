package com.dragosghinea.royale.internal.utils.material;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StandardMaterialStringProcessor implements MaterialStringProcessor {

    private static final Pattern NUMERIC_ID_PATTERN = Pattern.compile("(\\d+)(?::(\\d+).*)?");

    /**
     * Parses a material string in the format of "id:data" or "id" into an array of two integers.
     *
     * @return An array of two integers, where the first integer is the material id and the second integer is the data value
     * or null if the material string is invalid.
     */
    private static int[] parseNumericId(String materialString) {

        Matcher matching = NUMERIC_ID_PATTERN.matcher(materialString);
        if (matching.matches()) {
            return null;
        }

        int[] result = new int[2];

        result[0] = Integer.parseInt(matching.group(1));
        result[1] = matching.group(2) != null ? Integer.parseInt(matching.group(2)) : 0;
        return result;
    }

    @Override
    public Material processMaterial(String materialString) {
        materialString = materialString.toUpperCase();

        int[] numericId = parseNumericId(materialString);
        if (numericId != null) {
            return Material.getMaterial(
                    LegacyIdHackyWay.getMaterialFromIntegerId(numericId[0])
            );
        }

        return Material.matchMaterial(materialString);
    }

    @Override
    public XMaterial processXMaterial(String materialString) {
        materialString = materialString.toUpperCase();

        int[] numericId = parseNumericId(materialString);
        if (numericId != null) {
            return XMaterial.matchXMaterial(
                    LegacyIdHackyWay.getMaterialFromIntegerId(numericId[0])+":"+numericId[1]
            ).orElse(XMaterial.BARRIER);
        }

        return XMaterial.matchXMaterial(materialString).orElse(XMaterial.BARRIER);
    }
}
