package me.Muell.server.trade;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.types.Items;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class TradeObject {

    private Player player, partner;

    private Tradestate state;
    private Inventory inventory;
    private ArrayList<Ability> abis = new ArrayList<>();
    private ArrayList<Ability> abisremove = new ArrayList<>();

    private TradeObject partnertrade;

    TradeObject(Player p, Player target) {

	Inventory invplayer = Bukkit.createInventory(null, 54, "Trade");

	invplayer.setItem(13, Items.createitem(Material.CHEST, "add a Ability", null));
	invplayer.setItem(22, Items.createitem(Material.EMERALD, "ready", null));
	invplayer.setItem(31, Items.createitem(Material.FENCE, "abort", null));

	this.player = p;
	this.partner = target;
	this.setState(Tradestate.TRADING);
	this.inventory = invplayer;
	Trade.trades.put(p, this);

	p.openInventory(invplayer);
	p.updateInventory();

    }

    public void delete() {

	Trade.trades.remove(player);

    }

    public Inventory getTradeInventory() {
	return inventory;
    }

    public Tradestate getState() {
	return state;
    }

    public void setState(Tradestate state) {
	this.state = state;
    }

    public ArrayList<Ability> getAbis() {
	return abis;
    }

    public Player getPartner() {
	return partner;
    }

    public TradeObject getgetPartnertrade() {
	return getPartnertrade();
    }

    public TradeObject getPartnertrade() {
	return partnertrade;
    }

    public void setPartnertrade(TradeObject partnertrade) {
	this.partnertrade = partnertrade;
    }

    public void update() {

	setState(Tradestate.TRADING);

	resetTradeinv();

	if (!getAbis().isEmpty()) {

	    int slot1 = 0;

	    for (Ability abis : getAbis()) {

		if (abis == null) {

		return; }

		getTradeInventory().setItem(slot1, abis.getInvItem());

		if (partnertrade.getState() != Tradestate.ADDING) {
		    getPartnertrade().getTradeInventory().setItem(slot1 + 5, abis.getInvItem());
		}

		slot1++;

		if (slot1 % 9 > 3) {
		    slot1 = slot1 + 5;

		}

	    }
	}

	if (!getPartnertrade().getAbis().isEmpty()) {

	    int slot2 = 0;

	    for (Ability abis : getPartnertrade().getAbis()) {

		if (abis == null) {

		return; }

		if (partnertrade.getState() != Tradestate.ADDING) {
		    getPartnertrade().getTradeInventory().setItem(slot2, abis.getInvItem());
		}

		getTradeInventory().setItem(slot2 + 5, abis.getInvItem());

		slot2++;

		if (slot2 % 9 > 3) {
		    slot2 = slot2 + 5;

		}

	    }
	}

	player.updateInventory();
	getPartner().updateInventory();

	return;

    }

    public void resetTradeinv() {

	if (getState() != Tradestate.ADDING) {

	    getTradeInventory().clear();
	    getTradeInventory().setItem(13, Items.createitem(Material.CHEST, "add a Ability", null));
	    getTradeInventory().setItem(22, Items.createitem(Material.EMERALD, "ready", null));
	    getTradeInventory().setItem(31, Items.createitem(Material.FENCE, "abort", null));
	}

	if (partnertrade.getState() != Tradestate.ADDING) {

	    partnertrade.getTradeInventory().clear();
	    partnertrade.getTradeInventory().setItem(13, Items.createitem(Material.CHEST, "add a Ability", null));
	    partnertrade.getTradeInventory().setItem(22, Items.createitem(Material.EMERALD, "ready", null));
	    partnertrade.getTradeInventory().setItem(31, Items.createitem(Material.FENCE, "abort", null));
	}

    }

    public void addItem(Ability abi) {

	if (getAbis().size() > 16) {

	    player.sendMessage(ChatColor.RED + "You can not trade more than 16 abilities in one trade!");
	    update();

	    return;

	}

	getAbis().add(abi);

	update();

    }

    public void removeItem(Ability abi) {

	if (getAbis().contains(abi)) {

	    getAbis().remove(abi);

	    update();

	}

    }

    public void ready() {

	setState(Tradestate.READY);

	player.sendMessage(ChatColor.GREEN + "You are ready!");
	getPartner().sendMessage(ChatColor.GREEN + player.getName() + " is ready!");

	if (getState() == (Tradestate.READY) && getPartnertrade().getState() == (Tradestate.READY)) {

	    getTradeInventory().setItem(22, Items.createitem(Material.REDSTONE, "accept", null));
	    getPartnertrade().getTradeInventory().setItem(22, Items.createitem(Material.REDSTONE, "accept", null));
	    player.updateInventory();
	    getPartner().updateInventory();

	    return;

	}

    }

    public void finish() {

	PlayerData pd = Main.getPlayerData(player);
	PlayerData td = Main.getPlayerData(getPartner());

	setState(Tradestate.ACCEPTED);

	player.sendMessage(ChatColor.GREEN + "You accepted the trade!");
	getPartner().sendMessage(ChatColor.GREEN + player.getName() + " accepted the trade!");

	if (getState() == Tradestate.ACCEPTED && getPartnertrade().getState() == Tradestate.ACCEPTED) {

	    if (!getAbis().isEmpty()) {

		for (Ability abis : getAbis()) {

		    td.addAbility(abis);
		    new Loader().removeAbility(abis, pd.getPlayer());
		}

	    }

	    if (!getPartnertrade().getAbis().isEmpty()) {

		for (Ability abis : getPartnertrade().getAbis()) {

		    pd.addAbility(abis);
		    new Loader().removeAbility(abis, td.getPlayer());
		}
	    }

	    setState(Tradestate.NOTTRADING);
	    partnertrade.setState(Tradestate.NOTTRADING);

	    player.sendMessage(ChatColor.DARK_GREEN + "The Trade has been successful!");
	    partner.sendMessage(ChatColor.DARK_GREEN + "The Trade has been successful!");

	    player.closeInventory();
	    partner.closeInventory();

	    delete();
	    getPartnertrade().delete();

	    return;

	}

    }

    public void abort() {

	setState(Tradestate.NOTTRADING);
	getPartnertrade().setState(Tradestate.NOTTRADING);

	player.sendMessage(ChatColor.RED + "The Trade has been cancelled!");
	partner.sendMessage(ChatColor.RED + "The Trade has been cancelled!");

	player.closeInventory();
	partner.closeInventory();

	delete();
	getPartnertrade().delete();

	return;

    }

    public ArrayList<Ability> getAbisremove() {
	return abisremove;
    }

    public void setAbisremove(ArrayList<Ability> abisremove) {
	this.abisremove = abisremove;
    }

}
