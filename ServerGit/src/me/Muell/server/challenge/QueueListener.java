package me.Muell.server.challenge;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Muell.server.types.Inventories;

public class QueueListener implements Listener {

    @EventHandler
    public void onQueue(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	ItemStack item = e.getItem();

	if (item == null)
	    return;

	if (!item.hasItemMeta())
	    return;

	if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Queue")) {

	    e.setCancelled(true);
	    new Queue(p);
	    return;
	}

	if (item.getItemMeta().getDisplayName().equals("Leave the queue")) {

	    e.setCancelled(true);

	    new Inventories().onevsoneInventory(p);
	    Queue.playersinqueue.remove(p);
	    return;
	}

    }

}
