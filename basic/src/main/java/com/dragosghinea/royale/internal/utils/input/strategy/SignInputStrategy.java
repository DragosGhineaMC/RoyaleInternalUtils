package com.dragosghinea.royale.internal.utils.input.strategy;

import com.dragosghinea.royale.internal.utils.input.InputCfg;
import de.rapha149.signgui.SignGUI;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class SignInputStrategy implements InputStrategy {

    private final InputCfg inputCfg;

    @Override
    public CompletableFuture<String> fetchInput(Player player) {
        CompletableFuture<String> future = new CompletableFuture<>();
        SignGUI signGUI = SignGUI.builder()
                .setLine(1, inputCfg.getLines().get(0))
                .setLine(2, inputCfg.getLines().get(1))
                .setLine(3, inputCfg.getLines().get(2))
                .setHandler((p, lines) -> {

                    future.complete(lines.getLine(0));
                    return Collections.emptyList();
                })
                .build();

        signGUI.open(player);
        return future;
    }
}
