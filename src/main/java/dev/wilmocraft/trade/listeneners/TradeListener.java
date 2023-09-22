package dev.wilmocraft.trade.listeneners;

import dev.wilmocraft.trade.ui.TradeUI;

import org.bukkit.Material;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class TradeListener implements Listener {

    private HashMap<Player, Player> tradingPlayers = new HashMap<>();

    public void addPlayers(Player p1, Player p2){
        tradingPlayers.put(p1, p2);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(TradeUI.getInventory()))
            return;

        if (event.getSlotType() == InventoryType.SlotType.QUICKBAR)
            return;

        Player player = (Player)event.getWhoClicked();

        if (tradingPlayers.containsKey(player)) {
            if (!(event.getSlot() <= 8 || event.getSlot() == 17 || event.getSlot() >= 27))
                event.setCancelled(true);
        } else {
            if (!(event.getSlot() >= 17))
                event.setCancelled(true);
        }

        if (event.getSlot() == 17) {
            accept(player, event.getCurrentItem());
            event.setCancelled(true);
        }
    }

    public void accept(Player player, ItemStack item) {
        if (item.getType().equals(Material.RED_CONCRETE)) {
            item.setType(Material.GREEN_CONCRETE);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(player.getName());
            item.setItemMeta(meta);
        } else if (item.getType().equals(Material.GREEN_CONCRETE)){
            if (item.getItemMeta().getDisplayName().equals(player.getName())){
                item.setType(Material.RED_CONCRETE);
                ItemMeta meta = item.getItemMeta();

                meta.setDisplayName("Accept");
                item.setItemMeta(meta);
            } else {
                finishTrade(player.getOpenInventory().getTopInventory());
            }
        }
    }

    public void finishTrade(Inventory inventory) {
        List<HumanEntity> viewers = inventory.getViewers();
        Player p1;
        Player p2;

        if (tradingPlayers.containsKey((Player)viewers.get(0))) {
            p1 = (Player)viewers.get(0);
            p2 = (Player)viewers.get(1);
        } else {
            p1 = (Player)viewers.get(1);
            p2 = (Player)viewers.get(0);
        }

        p1.closeInventory();
        p2.closeInventory();

        for (int i = 0; i < 9; i++) {
            if (inventory.getItem(i) != null) {
                p2.getInventory().addItem(inventory.getItem(i));
            }

            if (inventory.getItem(i + 18) != null) {
                p1.getInventory().addItem(inventory.getItem(i + 18));
            }
        }

        tradingPlayers.remove(p1);
        TradeUI.clearInventory();
    }
}