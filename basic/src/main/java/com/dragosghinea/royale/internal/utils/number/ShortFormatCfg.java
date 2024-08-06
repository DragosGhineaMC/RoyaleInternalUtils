package com.dragosghinea.royale.internal.utils.number;

import com.dragosghinea.yaml.ConfigValues;
import com.dragosghinea.yaml.annotations.Comments;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortFormatCfg extends ConfigValues {

    @Comments({
            "The short formats are used to display numbers in a more compact way.",
            "1,000,000 -> 1M"
    })
    @JsonProperty("short-formats")
    private Map<String, ShortFormatPairCfg> shortFormats;
}
