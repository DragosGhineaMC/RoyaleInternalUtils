package com.dragosghinea.royale.internal.utils.input.strategy;

import com.cryptomorin.xseries.XMaterial;
import com.dragosghinea.royale.internal.utils.input.InputCfg;
import com.dragosghinea.royale.internal.utils.messages.StringMessageProcessorChain;
import com.dragosghinea.royale.internal.utils.messages.impl.StringMessageProcessorChainImpl;
import com.dragosghinea.royale.internal.utils.messages.impl.processor.ColorMessageProcessorImpl;
import com.dragosghinea.royale.internal.utils.messages.impl.processor.PlaceholderAPIMessageProcessorImpl;
import lombok.RequiredArgsConstructor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class AnvilInputStrategy implements InputStrategy {

    private final Plugin plugin;
    private final InputCfg inputCfg;

    private final StringMessageProcessorChain stringMessageProcessorChain = new StringMessageProcessorChainImpl();
    {
        stringMessageProcessorChain.addProcessor(new ColorMessageProcessorImpl());
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            stringMessageProcessorChain.addProcessor(new PlaceholderAPIMessageProcessorImpl());
        }
    }

    @Override
    public CompletableFuture<String> fetchInput(Player player) {
        CompletableFuture<String> future = new CompletableFuture<>();

        ItemStack paper = XMaterial.PAPER.parseItem();
        if (paper == null)
            throw new RuntimeException("Paper item is somehow null (should never happen)");

        ItemMeta meta = paper.getItemMeta();
        if (meta == null)
            throw new RuntimeException("ItemMeta is somehow null (should never happen)");

        meta.setDisplayName(" ");
        List<String> lore = new ArrayList<>();
        for (String line : inputCfg.getLines()) {
            lore.add(stringMessageProcessorChain.processMessage(player, line));
        }
        meta.setLore(lore);

        new AnvilGUI.Builder()
                .onClick((p, reply) -> {
                    future.complete(reply.getText());
                    return Collections.singletonList(AnvilGUI.ResponseAction.close());
                })
                .onClose(p -> future.complete(null))
                .itemLeft(paper)
                .title(" ")
                .text(" ")
                .plugin(plugin)
                .open(player);

        return future;
    }
}
