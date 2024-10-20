package com.dragosghinea.royale.internal.utils.input;

import com.dragosghinea.royale.internal.utils.input.strategy.AnvilInputStrategy;
import com.dragosghinea.royale.internal.utils.input.strategy.ChatInputStrategy;
import com.dragosghinea.royale.internal.utils.input.strategy.InputStrategy;
import com.dragosghinea.royale.internal.utils.input.strategy.SignInputStrategy;
import com.tcoded.folialib.impl.PlatformScheduler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

@Getter
public class InputFetcher {

    private final Plugin plugin;

    private final InputCfg inputCfg;

    private final PlatformScheduler scheduler;

    @Setter
    private InputStrategy inputStrategy;

    public InputFetcher(Plugin plugin, InputCfg inputCfg, PlatformScheduler scheduler) {
        this.plugin = plugin;
        this.inputCfg = inputCfg;
        this.scheduler = scheduler;

        switch(inputCfg.getInputType().toUpperCase()) {
            case "ANVIL":
                inputStrategy = new AnvilInputStrategy(plugin, inputCfg);
                break;
            case "SIGN":
                inputStrategy = new SignInputStrategy(inputCfg);
                break;
            default: //CHAT
                inputStrategy = new ChatInputStrategy(plugin, inputCfg, scheduler);
        }
    }

    public CompletableFuture<String> fetchInput(Player player) {
        try {
            return inputStrategy.fetchInput(player);
        }catch(RuntimeException e){
            e.printStackTrace();

            if (inputStrategy instanceof SignInputStrategy) {
                plugin.getLogger().info("Sign input strategy failed, switching to anvil input strategy");
                inputStrategy = new AnvilInputStrategy(plugin, inputCfg);
            }
            else if(inputStrategy instanceof AnvilInputStrategy){
                plugin.getLogger().info("Anvil input strategy failed, switching to chat input strategy");
                inputStrategy = new ChatInputStrategy(plugin, inputCfg, scheduler);
            }
            else {
                plugin.getLogger().info("Chat input strategy failed, giving up");
                return CompletableFuture.completedFuture(null);
            }

            return fetchInput(player);
        }
    }
}
