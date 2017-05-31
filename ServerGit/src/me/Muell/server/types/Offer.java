package me.Muell.server.types;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;

public class Offer {

    public static ArrayList<Offer> offers = new ArrayList<>();
    private OfflinePlayer p;
    private Ability ability;
    private int price;
    private long date;

    public Offer(OfflinePlayer p, Ability ability, int price) {

	this.p = p;
	this.ability = ability;
	this.price = price;
	this.date = System.currentTimeMillis();
	offers.add(this);
	Loader loader = new Loader();
	loader.removeAbility(ability, p);

    }

    public Offer(OfflinePlayer p, Ability ability, int price, long date) {

	this.p = p;
	this.ability = ability;
	this.price = price;
	this.date = date;
	offers.add(this);
    }

    public void buyOffer(Player buyer) {

	PlayerData buyerd = Main.getPlayerData(buyer);

	if (buyerd.getPoints() - getPrice() < 0) {
	    buyer.sendMessage(ChatColor.RED + "This offer is too expensive for you!");
	    buyer.sendMessage(ChatColor.RED + "Try to earn money and come back later");
	    return;
	}

	Loader loader = new Loader();
	offers.remove(this);
	loader.addAbility(ability, buyer.getName(), Bukkit.getConsoleSender(), false);
	buyer.sendMessage("You bought the offer of the ability " + ability.getName() + " from the player " + p.getName());
	loader.changePoints(-price, buyer, Bukkit.getConsoleSender(), false);
	loader.changePoints(price, p, Bukkit.getConsoleSender(), false);

    }

    public OfflinePlayer getP() {
	return p;
    }

    public void setP(Player p) {
	this.p = p;
    }

    public Ability getAbility() {
	return ability;
    }

    public void setAbility(Ability ability) {
	this.ability = ability;
    }

    public int getPrice() {
	return price;
    }

    public void setPrice(int price) {
	this.price = price;
    }

    public long getDate() {
	return date;
    }

    public void setDate(long date) {
	this.date = date;
    }

}
