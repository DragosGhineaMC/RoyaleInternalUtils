package com.dragosghinea.royale.internal.utils.input.strategy;

import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface InputStrategy {

    CompletableFuture<String> fetchInput(Player player);
}
