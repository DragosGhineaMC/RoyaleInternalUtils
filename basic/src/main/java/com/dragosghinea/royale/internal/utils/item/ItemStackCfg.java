package com.dragosghinea.royale.internal.utils.item;

import com.dragosghinea.yaml.ConfigValues;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemStackCfg extends ConfigValues {

    @JsonProperty("material")
    private String material;

    @JsonProperty("amount")
    private int amount = 1;

    @JsonProperty("name")
    private String name;

    @JsonProperty("lore")
    private List<String> lore;

}
