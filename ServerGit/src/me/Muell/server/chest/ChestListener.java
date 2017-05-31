package me.Muell.server.chest;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Muell.server.Main;

public class ChestListener implements Listener {

    @EventHandler
    public void onChestClose(InventoryCloseEvent e) {

	Player p = (Player) e.getPlayer();

	if (Chest.chestopening.contains(p)) {

	    new BukkitRunnable() {

		@Override
		public void run() {

		    p.openInventory(e.getInventory());
		}
	    }.runTask(Main.getPlugin());

	}

    }

    @EventHandler
    public void onChestClick(InventoryClickEvent e) {

	if (e.getClickedInventory() == null || e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {

	return; }

	if (!e.getCurrentItem().hasItemMeta())
	    return;

	if (e.getInventory().getName().equals("Chest opening")) {

	    e.setCancelled(true);
	    return;
	}

    }

}
