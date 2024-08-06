package com.dragosghinea.royale.internal.utils.input;

import com.dragosghinea.yaml.ConfigValues;
import com.dragosghinea.yaml.annotations.Comments;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputCfg extends ConfigValues {

    @Comments({
        "What input method should be used?",
        "Options: sign, anvil, chat",
        " ",
        "If your option is not available, the input will try to fall back to the next available option."
    })
    @JsonProperty("input-type")
    private String inputType = "sign";

    @Comments({
        "",
        "When chat input is used the player has 20 seconds to respond.",
        "If the player does not respond in time, the input will be considered invalid.",
        "The following message will be sent."
    })
    @JsonProperty("chat-took-too-long")
    private String chatTookTooLong = "&cYou took too long to enter an input.";

    @Comments({
        "",
        "For sign input, these would be the last three lines of the sign.",
        "For anvil input, these would be the lore of the item.",
        "For chat input, these would be the messages sent to the player."
    })
    @JsonProperty("lines")
    private List<String> lines = new ArrayList<>();
    {
        lines.add("^^^^^^^^^^^^^^^^");
        lines.add("Your input on");
        lines.add("the first line");
    }
}
