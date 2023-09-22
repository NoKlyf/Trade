package dev.wilmocraft.trade.ui;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.ChatColor.*;

public class TradeUI {
    private static Inventory gui = Bukkit.createInventory(null, 27, GOLD + "Trade");

    public static Inventory createInventory() {
        ItemStack divisor = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta divisorMeta = divisor.getItemMeta();

        divisorMeta.setDisplayName(" ");

        divisor.setItemMeta(divisorMeta);

        ItemStack finishTrade = new ItemStack(Material.RED_CONCRETE);
        ItemMeta finishTradeMeta = finishTrade.getItemMeta();

        finishTradeMeta.setDisplayName("Accept");

        finishTrade.setItemMeta(finishTradeMeta);

        for (int i = 9; i <= 16; i++) {
            gui.setItem(i, divisor);
        }

        gui.setItem(17, finishTrade);

        return gui;
    }

    public static void clearInventory() {
        gui = Bukkit.createInventory(null, 27, GOLD + "Trade");
        createInventory();
    }

    public static Inventory getInventory() {
        return gui;
    }
}