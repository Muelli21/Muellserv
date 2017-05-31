package me.Muell.server.types;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;
import org.bukkit.material.Wool;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class Items {

    public static ItemStack createitem(Material material, String displayname, List<String> lore) {

	ItemStack item = new ItemStack(material, 1);

	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setLore(lore);
	item.setItemMeta(meta);

	return item;

    }

    @SuppressWarnings("deprecation")
    public static ItemStack createGlassPane(DyeColor color, String displayname, List<String> lore) {

	ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, color.getData());

	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setLore(lore);
	item.setItemMeta(meta);

	return item;

    }

    public static ItemStack createitems(Material material, String displayname, List<String> lore, Integer amount) {

	ItemStack item = new ItemStack(material, amount);

	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setLore(lore);
	item.setItemMeta(meta);

	return item;

    }

    public static ItemStack createenchanteditem(Material material, String displayname, List<String> lore, Enchantment ench, Integer level) {

	ItemStack item = new ItemStack(material, 1);

	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setLore(lore);
	item.setItemMeta(meta);
	item.addUnsafeEnchantment(ench, level);

	return item;

    }

    public static ItemStack createenchanteditem(Material material, String displayname, List<String> lore, Enchantment ench1, Integer level1, Enchantment ench2, Integer level2) {

	ItemStack item = new ItemStack(material, 1);

	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setLore(lore);
	item.setItemMeta(meta);
	item.addUnsafeEnchantment(ench1, level1);
	item.addUnsafeEnchantment(ench2, level2);

	return item;

    }

    public static ItemStack createWool(DyeColor color, String displayname, List<String> lore) {

	Wool wool = new Wool(color);
	ItemStack item = wool.toItemStack();

	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setLore(lore);
	item.setItemMeta(meta);

	return item;

    }

    public static ItemStack createDye(DyeColor color, String displayname, List<String> lore) {

	Dye dye = new Dye();
	dye.setColor(color);
	ItemStack item = dye.toItemStack();
	item.setAmount(1);

	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setLore(lore);
	item.setItemMeta(meta);

	return item;
    }

    public static ItemStack createPotion(PotionType type, Integer level, String displayname, boolean splash) {

	Potion potion = new Potion(type, level);
	potion.setSplash(splash);

	ItemStack item = potion.toItemStack(1);
	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(displayname);
	item.setItemMeta(meta);

	return item;
    }

    public static ItemStack createHelmet(Color color, String displayname, List<String> lore) {

	ItemStack item = new ItemStack(Material.LEATHER_HELMET, 1);

	LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setLore(lore);
	meta.setColor(color);
	item.setItemMeta(meta);

	return item;

    }

    public static ItemStack createChest(Color color, String displayname, List<String> lore) {

	ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE, 1);

	LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setLore(lore);
	meta.setColor(color);
	item.setItemMeta(meta);

	return item;

    }

    public static ItemStack createLeggings(Color color, String displayname, List<String> lore) {

	ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS, 1);

	LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setLore(lore);
	meta.setColor(color);
	item.setItemMeta(meta);

	return item;

    }

    public static ItemStack createBoots(Color color, String displayname, List<String> lore) {

	ItemStack item = new ItemStack(Material.LEATHER_BOOTS, 1);

	LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
	meta.setDisplayName(displayname);
	meta.setLore(lore);
	meta.setColor(color);
	item.setItemMeta(meta);

	return item;

    }

    @SuppressWarnings("deprecation")
    public static ItemStack createSkullHead(Player p, String displayname) {

	ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3, (byte) SkullType.PLAYER.ordinal());
	SkullMeta sm = (SkullMeta) skull.getItemMeta();
	sm.setOwner(p.getName());
	sm.setDisplayName(displayname);
	skull.setItemMeta(sm);

	return skull;

    }

}