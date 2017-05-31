package me.Muell.server.trade;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.chest.Chest;
import me.Muell.server.types.Items;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class Tradeup implements Listener {

    public static HashMap<Player, TradeupObject> tradeups = new HashMap<>();

    public void TradeUp(Player p) {

	new TradeupObject(p);
    }

    class TradeupObject {

	private ArrayList<Ability> abilitiesputintotrade = new ArrayList<>();
	private Player p;
	private Inventory tradeupinv;

	public TradeupObject(Player p) {

	    this.p = p;
	    tradeups.put(p, this);

	    Inventory inv = Bukkit.createInventory(null, 9, "Tradeup");
	    inv.setItem(0, Items.createitem(Material.CHEST, "add a Ability", null));
	    this.tradeupinv = inv;
	    p.openInventory(inv);

	}

	public ArrayList<Ability> getAbilitiesputintotrade() {
	    return abilitiesputintotrade;
	}

	public void setAbilitiesputintotrade(ArrayList<Ability> abilitiesputintotrade) {
	    this.abilitiesputintotrade = abilitiesputintotrade;
	}

	public Player getP() {
	    return p;
	}

	public void setP(Player p) {
	    this.p = p;
	}

	public Inventory getTradeupinv() {
	    return tradeupinv;
	}

	public void setTradeupinv(Inventory tradeupinv) {
	    this.tradeupinv = tradeupinv;
	}

	public void remove(Ability abi) {

	    getAbilitiesputintotrade().remove(abi);
	    update();
	}

	public void add(Ability abi) {

	    getAbilitiesputintotrade().add(abi);
	    update();
	}

	public void update() {

	    if (getAbilitiesputintotrade().size() >= 5) {

		for (Ability abi : getAbilitiesputintotrade())
		    new Loader().removeAbility(abi, p);

		new Chest(p);
		return;
	    }

	    getTradeupinv().clear();
	    getTradeupinv().setItem(0, Items.createitem(Material.CHEST, "add a Ability", null));

	    for (Ability abilities : getAbilitiesputintotrade())
		getTradeupinv().addItem(abilities.getInvItem());

	    p.openInventory(getTradeupinv());
	    getP().updateInventory();
	}
    }

    @EventHandler
    public void onInv(InventoryClickEvent e) {

	Player p = (Player) e.getWhoClicked();
	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = e.getCurrentItem();

	if (e.getClickedInventory() == null || item == null || item.getType() == Material.AIR)
	    return;

	if (!item.hasItemMeta())
	    return;

	TradeupObject trade = tradeups.get(p);
	if (trade == null)
	    return;

	if (e.getInventory().getName().equalsIgnoreCase("tradeup a Ability")) {

	    e.setCancelled(true);

	    if (item.getItemMeta().getDisplayName().equals("back")) {
		trade.update();
		return;
	    }

	    Ability abi = new Loader().getAbility(item.getItemMeta().getDisplayName());
	    if (abi == null)
		return;
	    trade.add(abi);
	}

	if (e.getClickedInventory().getName().equals("Tradeup")) {

	    e.setCancelled(true);

	    if (item.getItemMeta().getDisplayName().equals("add a Ability")) {

		Inventory inv = Bukkit.createInventory(null, 54, "tradeup a Ability");

		ArrayList<Ability> realabis = new ArrayList<>();

		for (Ability abi : pd.ownedAbilities)
		    realabis.add(abi);

		for (Ability abi : trade.getAbilitiesputintotrade())
		    realabis.remove(abi);

		for (Ability abi : realabis)
		    inv.addItem(abi.getInvItem());

		inv.setItem(53, Items.createWool(DyeColor.RED, "back", null));

		p.openInventory(inv);
		p.updateInventory();
		p.sendMessage("Choose a Ability to add to the tradeup!");
		return;
	    }

	    if (e.isRightClick()) {

		Ability abi = new Loader().getAbility(item.getItemMeta().getDisplayName());

		if (abi == null)
		    return;
		trade.remove(abi);
	    }
	}

    }
}
