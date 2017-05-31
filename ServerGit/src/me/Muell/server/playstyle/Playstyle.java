package me.Muell.server.playstyle;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import me.Muell.server.types.Items;

public enum Playstyle {

    SOUP(
	    "Souper",
	    Items.createitem(Material.MUSHROOM_SOUP, "Soup", null)),
    POT(
	    "Potter",
	    Items.createPotion(PotionType.INSTANT_HEAL, 2, "Pot", true)),
    ROD(
	    "Rodder",
	    Items.createitem(Material.FISHING_ROD, "Rod", null));

    String name;
    ItemStack invItem;

    private Playstyle(String name, ItemStack invItem) {

	this.invItem = invItem;
	this.name = name;
    }

    public String getName() {

	return this.name;
    }
    public ItemStack getInvItem() {

	return this.invItem;
    }

}