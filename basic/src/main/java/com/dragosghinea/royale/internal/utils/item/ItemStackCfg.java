package com.dragosghinea.royale.internal.utils.item;

import com.dragosghinea.yaml.ConfigValues;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ItemStackCfg extends ConfigValues {

    @JsonProperty("material")
    private String material;

    @JsonProperty("amount")
    @Builder.Default
    private int amount = 1;

    @JsonProperty("name")
    private String name;

    @JsonProperty("lore")
    private List<String> lore;

    @JsonProperty("custom-model-data")
    private Integer customModelData;

}
