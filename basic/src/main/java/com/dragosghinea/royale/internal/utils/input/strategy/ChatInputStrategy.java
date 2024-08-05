package com.dragosghinea.royale.internal.utils.input.strategy;

import com.dragosghinea.royale.internal.utils.input.InputCfg;
import com.dragosghinea.royale.internal.utils.messages.MessageSender;
import com.dragosghinea.royale.internal.utils.messages.StringMessageProcessorChain;
import com.dragosghinea.royale.internal.utils.messages.impl.StringMessageProcessorChainImpl;
import com.dragosghinea.royale.internal.utils.messages.impl.processor.ColorMessageProcessorImpl;
import com.dragosghinea.royale.internal.utils.messages.impl.processor.PlaceholderAPIMessageProcessorImpl;
import com.dragosghinea.royale.internal.utils.messages.impl.sender.PlainMessageSenderImpl;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class ChatInputStrategy implements InputStrategy {

    private final Plugin plugin;
    private final InputCfg inputCfg;

    private final MessageSender messageSender = new PlainMessageSenderImpl();
    private final StringMessageProcessorChain stringMessageProcessorChain = new StringMessageProcessorChainImpl();

    {
        stringMessageProcessorChain.addProcessor(new ColorMessageProcessorImpl());
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            stringMessageProcessorChain.addProcessor(new PlaceholderAPIMessageProcessorImpl());
        }
    }


    @Override
    public CompletableFuture<String> fetchInput(Player player) {
        CompletableFuture<String> onChatInput = new CompletableFuture<>();
        ChatHandler listener = new ChatHandler(player, onChatInput);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (onChatInput.isDone()) return;

            HandlerList.unregisterAll(listener);
            messageSender.sendMessage(player, stringMessageProcessorChain.processMessage(player, inputCfg.getChatTookTooLong()));
        }, 500);
        return onChatInput;
    }

    private static class ChatHandler implements Listener {

        private final Player player;
        private final CompletableFuture<String> onChatInput;

        public ChatHandler(Player player, CompletableFuture<String> onChatInput) {
            this.player = player;
            this.onChatInput = onChatInput;
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void chatListener(AsyncPlayerChatEvent e) {
            if (e.getPlayer().equals(player)) {
                e.setCancelled(true);
                HandlerList.unregisterAll(this);

                String message = ChatColor.stripColor(e.getMessage());
                onChatInput.complete(message);
            }
        }

        @EventHandler
        public void inventoryOpen(InventoryOpenEvent event) {
            if (event.getPlayer().equals(player))
                event.setCancelled(true);
        }

        @EventHandler
        public void onCommand(PlayerCommandPreprocessEvent event) {
            if (event.getPlayer().equals(player))
                event.setCancelled(true);
        }
    }
}
