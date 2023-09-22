package dev.wilmocraft.trade;

import dev.wilmocraft.trade.commands.TradeCommand;
import dev.wilmocraft.trade.listeneners.TradeListener;

import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public final class Trade extends JavaPlugin {
    private final TradeListener tradeListener = new TradeListener();

    @Override
    public void onEnable() {
        getLogger().info("Trade Plugin loaded with Bukkit version: " + Bukkit.getVersion());
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("Stopping Trade Plugin!");
    }

    private void registerCommands() {
        getCommand("trade").setExecutor(new TradeCommand(tradeListener));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(tradeListener, this);
    }
}