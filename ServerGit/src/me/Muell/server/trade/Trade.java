package me.Muell.server.trade;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.types.Items;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class Trade implements Listener {

    public static HashMap<Player, TradeObject> trades = new HashMap<>();

    // Should work well
    @EventHandler
    public void onTradeInventoryClose(InventoryCloseEvent e) {

	Player p = (Player) e.getPlayer();

	TradeObject trade = trades.get(p);
	if (trade == null)
	    return;

	if (e.getInventory().equals(trade.getTradeInventory())) {

	    if (trade.getState() == Tradestate.NOTTRADING)
		return;

	    new BukkitRunnable() {

		@Override
		public void run() {

		    p.openInventory(e.getInventory());
		    return;

		}
	    }.runTask(Main.getPlugin());

	    return;

	}

    }

    @EventHandler
    public void onTradeInventoryClick(InventoryClickEvent e) {

	Player p = (Player) e.getWhoClicked();
	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = e.getCurrentItem();

	if (e.getClickedInventory() == null || e.getCurrentItem() == null
		|| e.getCurrentItem().getType() == Material.AIR) {

	    return;
	}

	TradeObject trade = trades.get(p);
	if (trade == null)
	    return;

	e.setCancelled(true);

	if (e.getClickedInventory().getName().equals("Trade")) {

	    e.setCancelled(true);

	    if (item.hasItemMeta() && item.getItemMeta().getDisplayName().equals("abort")) {

		trade.abort();

		return;
	    }

	    if (trade.getState() == Tradestate.READY) {

		if (item.hasItemMeta() && item.getItemMeta().getDisplayName().equals("accept")) {

		    trade.finish();

		    return;

		}
	    }

	    if (trade.getState() == Tradestate.ADDING) {

		if (item.hasItemMeta() && item.getItemMeta().getDisplayName().equals("back")) {

		    trade.update();

		    return;
		}

		Ability ability = new Loader().getAbility(item.getItemMeta().getDisplayName());

		if (ability == null)
		    return;

		trade.addItem(ability);

		return;

	    }

	    if (trade.getState() == Tradestate.TRADING) {

		if (item.hasItemMeta() && item.getItemMeta().getDisplayName().equals("add a Ability")) {

		    trade.setState(Tradestate.ADDING);

		    Inventory inv = trade.getTradeInventory();

		    inv.clear();

		    inv.setItem(0, Items.createitem(Material.REDSTONE, "back", null));
		    inv.setItem(8, Items.createitem(Material.REDSTONE, "next", null));

		    ArrayList<Ability> realabilities = new ArrayList<>();

		    for (Ability abi : pd.ownedAbilities) {

			realabilities.add(abi);

		    }

		    for (Ability abi : trade.getAbis()) {

			realabilities.remove(abi);
		    }

		    for (Ability abi : realabilities) {

			inv.addItem(abi.getInvItem());

		    }

		    p.updateInventory();
		    p.sendMessage("Choose a Ability to trade!");

		    return;
		}

		if (item.hasItemMeta() && item.getItemMeta().getDisplayName().equals("ready")) {

		    trade.ready();

		    return;

		}

		if (e.isRightClick()) {

		    Ability ability = new Loader().getAbility(item.getItemMeta().getDisplayName());

		    trade.removeItem(ability);

		    return;

		}

	    }

	}

    }
}
