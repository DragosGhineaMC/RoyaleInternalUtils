package com.dragosghinea.royale.internal.utils.number;

import com.dragosghinea.yaml.ConfigValues;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortFormatPairCfg extends ConfigValues {

    @JsonProperty("number-of-zeros")
    private int numberOfZeros;

    @JsonProperty("unit-name")
    private String unitName;
}
