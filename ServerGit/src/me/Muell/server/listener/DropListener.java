package me.Muell.server.listener;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class DropListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {

	Player p = e.getPlayer();

	PlayerData pd = Main.getPlayerData(p);

	if (!pd.isIngame()) {

	    if (pd.isInAdminmode()) {

		return;
	    }

	    e.setCancelled(true);

	    p.sendMessage(ChatColor.RED + "You can not drop items if you are not ingame!");

	}

	if (pd.isIngame()) {

	    ItemStack item = (ItemStack) e.getItemDrop().getItemStack();

	    if (!item.hasItemMeta()) {

		return;
	    }

	    String abiname = item.getItemMeta().getDisplayName();
	    String word = abiname.split(" ")[0];

	    Ability abi = new Loader().getAbility(word);

	    if (abi == null)
		return;

	    e.setCancelled(true);
	    p.sendMessage(ChatColor.RED + "You can not drop Abilityitems!");

	}

    }

    @EventHandler
    public void ondespawn(ItemDespawnEvent e) {

	Item item = e.getEntity();

	item.getWorld().playEffect(item.getLocation(), Effect.COLOURED_DUST, 10);

    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {

	Player p = e.getPlayer();

	PlayerData pd = Main.getPlayerData(p);

	if (!pd.isIngame()) {

	    e.setCancelled(true);

	}

    }

}
