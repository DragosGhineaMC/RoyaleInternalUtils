package com.dragosghinea.royale.internal.utils.input;

import com.dragosghinea.royale.internal.utils.number.RoyaleNumberFormat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class InputFetcherWithNumeric extends InputFetcher {

    private final List<RoyaleNumberFormat> numberFormats;

    private String preprocessInput(String input) {
        int index = -1;

        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i)) || input.charAt(i) == '-') {
                index = i;
                break;
            }
        }

        if (index != -1) {
            input = input.substring(index);
        }

        return input;
    }

    private BigDecimal parseNumericInput(String input) {
        input = preprocessInput(input);

        for (RoyaleNumberFormat numberFormat : numberFormats) {
            try {
                return numberFormat.fromFormat(input);
            } catch (NumberFormatException ignored) {
            }
        }

        throw new NumberFormatException("Invalid number format");
    }

    public InputFetcherWithNumeric(Plugin plugin, InputCfg inputCfg, List<RoyaleNumberFormat> numberFormats) {
        super(plugin, inputCfg);
        this.numberFormats = numberFormats;
    }

    public CompletableFuture<BigDecimal> fetchNumericInput(Player player) {
        CompletableFuture<BigDecimal> future = new CompletableFuture<>();

        super.fetchInput(player).thenAccept(input -> {
            try {
                future.complete(parseNumericInput(input));
            } catch (NumberFormatException e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }
}
