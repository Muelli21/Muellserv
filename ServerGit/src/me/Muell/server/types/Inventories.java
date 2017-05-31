package me.Muell.server.types;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import me.Muell.server.Main;

public class Inventories {

    Plugin plugin = Main.getPlugin();
    Server server = Bukkit.getServer();

    public void clearInventoy(Player p) {

	p.setGameMode(GameMode.ADVENTURE);
	p.setMaxHealth(20D);
	p.setHealth(20D);
	p.setFoodLevel(100);

	p.getInventory().clear();

	p.getInventory().setArmorContents(null);

	for (PotionEffect effect : p.getActivePotionEffects())
	    p.removePotionEffect(effect.getType());

	p.updateInventory();

    }

    @SuppressWarnings("deprecation")
    public void adminInventoy(Player p) {

	clearInventoy(p);

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    ps.hidePlayer(p);

	}

	Inventory inv = p.getInventory();

	p.setGameMode(GameMode.CREATIVE);

	ItemStack i = new ItemStack(Material.STICK);
	ItemMeta meta = i.getItemMeta();
	meta.setDisplayName("Knockback-Test");
	meta.addEnchant(Enchantment.KNOCKBACK, 20, true);
	i.setItemMeta(meta);
	inv.setItem(0, i);

	ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3, (byte) SkullType.PLAYER.ordinal());
	SkullMeta sm = (SkullMeta) skull.getItemMeta();
	sm.setOwner(p.getName());
	sm.setDisplayName(ChatColor.DARK_GREEN + "Playerchecker");
	skull.setItemMeta(sm);
	inv.setItem(4, skull);

	inv.setItem(8, Items.createitem(Material.IRON_FENCE, ChatColor.DARK_AQUA + "PlayerFreezer", null));

	p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1));

	p.updateInventory();

    }

    public void joinInventory(Player p) {

	clearInventoy(p);

	p.getInventory().setItem(0, Items.createitem(Material.CHEST, ChatColor.WHITE + "Lobbywarps", null));
	p.getInventory().setItem(2, Items.createitem(Material.EXP_BOTTLE, ChatColor.YELLOW + "Playstyle", null));
	p.getInventory().setItem(4, Items.createitem(Material.NETHER_STAR, ChatColor.GOLD + "Navigation", null));
	p.getInventory().setItem(6, Items.createitem(Material.EYE_OF_ENDER, ChatColor.GREEN + "Hide Players", null));
	p.getInventory().setItem(8,
		Items.createitem(Material.ANVIL, ChatColor.DARK_GREEN + "Kitcreation", Arrays.asList(ChatColor.GRAY + "create a custom Kit ", ChatColor.GRAY + "with your abilities")));

	p.updateInventory();

    }

    public void onevsoneInventory(Player p) {

	clearInventoy(p);

	p.getInventory().setItem(0, Items.createitem(Material.STICK, ChatColor.RED + "Challenger", null));
	p.getInventory().setItem(4, Items.createitem(Material.EYE_OF_ENDER, ChatColor.RED + "Queue", null));
	p.getInventory().setItem(8, Items.createitem(Material.SKULL_ITEM, ChatColor.GOLD + "ShadowFight", null));
	p.updateInventory();

    }

    public void kitcreationInventory(Player p) {

	clearInventoy(p);

	p.getInventory().setItem(4, Items.createitem(Material.STICK, ChatColor.GOLD + "Trade", Arrays.asList(ChatColor.GRAY + "Trade your abilities with ", ChatColor.GRAY + "other players")));
	p.getInventory().setItem(0, Items.createitem(Material.CHEST, ChatColor.RED + "Buy a chest", null));

	p.updateInventory();

    }

    public void MarketInventory(Player p) {

	clearInventoy(p);

	p.getInventory().setItem(3, Items.createitem(Material.ENCHANTMENT_TABLE, ChatColor.GOLD + "Market", Arrays.asList(ChatColor.GRAY + "Buy abilities from other players")));
	p.getInventory().setItem(5, Items.createitem(Material.CHEST, ChatColor.RED + "Sell abilities", Arrays.asList(ChatColor.GRAY + "Sell your own abilities")));

	p.updateInventory();

    }

    public void ehgInventory(Player p) {

	clearInventoy(p);

	p.getInventory().setItem(0, Items.createitem(Material.STONE_SWORD, "Sword", null));

	p.getInventory().setItem(13, new ItemStack(Material.RED_MUSHROOM, 32));
	p.getInventory().setItem(14, new ItemStack(Material.BROWN_MUSHROOM, 32));
	p.getInventory().setItem(15, new ItemStack(Material.BOWL, 32));

	for (int i = 1; i <= 32; i++)
	    p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));

	p.updateInventory();

    }

    public void potInventory(Player p) {

	clearInventoy(p);

	p.getInventory().setItem(0, Items.createitem(Material.STONE_SWORD, "Sword", null));
	p.setGameMode(GameMode.SURVIVAL);
	p.getInventory().setItem(1, new ItemStack(Material.GOLDEN_CARROT, 64));
	p.getInventory().setItem(2, new ItemStack(Material.ENDER_PEARL, 16));

	p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1));

	p.getInventory().setHelmet(Items.createHelmet(Color.ORANGE, "Helmet", null));
	p.getInventory().setChestplate(Items.createChest(Color.ORANGE, "Chest", null));
	p.getInventory().setLeggings(new ItemStack(Items.createLeggings(Color.ORANGE, "Leggings", null)));
	p.getInventory().setBoots(Items.createBoots(Color.ORANGE, "Boots", null));

	for (int i = 1; i <= 33; i++)
	    p.getInventory().addItem(Items.createPotion(PotionType.INSTANT_HEAL, 2, "Heal", true));

	p.updateInventory();

    }

    public void survivorInventory(Player p) {

	clearInventoy(p);

	p.getInventory().setItem(0, Items.createitem(Material.STONE_SWORD, "Sword", null));
	p.getInventory().setItem(1, new ItemStack(Material.FISHING_ROD));

	p.setMaxHealth(240D);
	p.setHealth(240D);

	p.updateInventory();

    }

}
