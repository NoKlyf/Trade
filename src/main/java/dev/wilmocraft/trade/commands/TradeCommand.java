package dev.wilmocraft.trade.commands;

import dev.wilmocraft.trade.listeneners.TradeListener;
import dev.wilmocraft.trade.ui.TradeUI;

import org.bukkit.Bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.inventory.Inventory;

import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TradeCommand implements CommandExecutor {
    private final Map<Player, Player> tradeRequest = new HashMap<>();
    private final TradeListener listener;

    public TradeCommand(TradeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(RED + "This command should only be executed by players!");
            return true;
        }

        Player player = (Player)sender;

        if (args.length > 2 || args.length < 1) {
            player.sendMessage(RED + "Usage: /trade <ask | accept> <player>");
            return true;
        }

        if (args.length == 2) {
            if (!args[0].equals("ask")) {
                player.sendMessage(RED + "Usage: /trade <ask | accept> <player>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                player.sendMessage(RED + "Player not found!");
                return true;
            }

            if (target.equals(player)) {
                player.sendMessage(RED + "You can't trade with yourself!");
                return true;
            }

            player.sendMessage(GREEN + "Sent trade request to " + YELLOW + target.getName());
            tradeRequest.put(target, player);

            target.sendMessage(YELLOW + player.getName() + GREEN + " sent you a trade request. type '/trade accept' to accept the request");
        }

        if (args.length == 1) {
            if (!args[0].equals("accept")) {
                player.sendMessage(RED + "Usage: /trade <ask | accept> <player>");
                return true;
            }

            if (!tradeRequest.containsKey(player)) {
                player.sendMessage(GREEN + "You don't have any pending trade request");
                return true;
            }

            Player target = tradeRequest.get(player);

            if (!Bukkit.getOnlinePlayers().contains(target)) {
                player.sendMessage(RED + "The player is not online anymore!");
                tradeRequest.remove(player);

                return true;
            }

            Inventory ui = TradeUI.createInventory();

            player.openInventory(ui);
            target.openInventory(ui);

            tradeRequest.remove(player);

            listener.addPlayers(player, target);
        }

        return true;
    }
}