package me.Muell.server.Guis;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import me.Muell.server.Main;

public class AbstractGuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

	Inventory inv = e.getClickedInventory();
	int slot = e.getSlot();
	Player p = (Player) e.getWhoClicked();

	AbstractGui gui = AbstractGui.Guis.get(inv);

	if (gui == null)
	    return;

	if (p != gui.getP())
	    return;

	e.setCancelled(true);

	if (e.isRightClick() && (gui.getRightaction().get(slot) != null)) {

	    gui.getRightaction().get(slot).click(p);
	}

	if (e.isLeftClick() && (gui.getLeftaction().get(slot) != null)) {

	    gui.getLeftaction().get(slot).click(p);
	}
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {

	Inventory inv = e.getInventory();
	Player p = (Player) e.getPlayer();

	AbstractGui gui = AbstractGui.Guis.get(inv);

	if (gui == null)
	    return;

	if (!gui.isCloseable()) {

	    new BukkitRunnable() {

		@Override
		public void run() {

		    p.openInventory(gui.getInv());
		    return;
		}
	    }.runTask(Main.getPlugin());

	    return;
	}

	if (p != gui.getP())
	    return;

	if (gui.getInv() == p.getInventory())
	    gui.delete();
    }

}
