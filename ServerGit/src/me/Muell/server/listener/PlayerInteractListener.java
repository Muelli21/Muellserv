package me.Muell.server.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Muell.server.Main;
import me.Muell.server.Guis.LobbywarpsGui;
import me.Muell.server.Guis.MarketGui;
import me.Muell.server.Guis.NavigationGui;
import me.Muell.server.Guis.OverviewGui;
import me.Muell.server.Guis.SellGui;
import me.Muell.server.abilities.AbilityChooseListener;
import me.Muell.server.arena.ArenaManager;
import me.Muell.server.bot.CustomZombie;
import me.Muell.server.types.DisguiseManager;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.Inventories;
import me.Muell.server.types.Items;
import me.Muell.server.types.PlayerData;
import net.minecraft.server.v1_7_R4.WorldServer;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void Interact(PlayerInteractEvent e) {

	Player p = (Player) e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	ItemStack item = p.getItemInHand();
	if (e.getAction().toString().contains("CLICK")) {

	    if (item.hasItemMeta()) {

		if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Navigation")) {

		    e.setCancelled(true);
		    new NavigationGui(p);
		    return;
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Lobbywarps")) {

		    e.setCancelled(true);
		    new LobbywarpsGui(p);
		    return;
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Hide Players")) {

		    e.setCancelled(true);
		    p.setItemInHand(Items.createitem(Material.ENDER_PEARL, ChatColor.DARK_GREEN + "Show Players", null));
		    DisguiseManager disguise = new DisguiseManager();
		    disguise.hideall(p);
		    return;
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + "Show Players")) {

		    e.setCancelled(true);
		    p.setItemInHand(Items.createitem(Material.EYE_OF_ENDER, ChatColor.GREEN + "Hide Players", null));
		    pd.setAllowedtoseeplayers(true);
		    DisguiseManager disguise = new DisguiseManager();
		    disguise.showCustom(p);
		    return;
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Playstyle")) {

		    e.setCancelled(true);

		    p.performCommand("playstyle");
		    return;
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + "Kitcreation")) {

		    e.setCancelled(true);

		    DisguiseManager disguise = new DisguiseManager();
		    disguise.showCustom(p);

		    Inventories inv = new Inventories();
		    inv.kitcreationInventory(p);

		    World world = Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.kitcreation.world"));
		    double x = Main.getPlugin().getConfig().getDouble("cords.kitcreation.x");
		    double y = Main.getPlugin().getConfig().getDouble("cords.kitcreation.y");
		    double z = Main.getPlugin().getConfig().getDouble("cords.kitcreation.z");
		    float yaw = (float) Main.getPlugin().getConfig().getDouble("cords.kitcreation.yaw");
		    float pitch = (float) Main.getPlugin().getConfig().getDouble("cords.kitcreation.pitch");

		    Location kitcreation = (new Location(world, x, y, z, yaw, pitch));
		    p.teleport(kitcreation);
		    pd.setGamemode(Gamemode.MARKET);
		    return;
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "ShadowFight")) {

		    p.closeInventory();
		    p.getInventory().clear();

		    pd.setIngame(true);

		    final WorldServer world1 = ((CraftWorld) p.getWorld()).getHandle();
		    final CustomZombie zombie = new CustomZombie(world1, p, pd.getBotspeed(), pd.getBothealth(), false);

		    ArenaManager ab = new ArenaManager();
		    ab.arenaManagerBot(p, zombie.getBukkitEntity());

		    AbilityChooseListener abichoose = new AbilityChooseListener();
		    abichoose.abilityChoosMode(p);

		    p.sendMessage(Main.pre + "Your bot has been created!");
		    p.getWorld().playSound(p.getLocation(), Sound.CLICK, 100, 100);

		    e.setCancelled(true);
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Oldschool ShadowFight")) {

		    p.closeInventory();
		    p.getInventory().clear();

		    pd.setIngame(true);

		    final WorldServer world1 = ((CraftWorld) p.getWorld()).getHandle();
		    final CustomZombie zombie = new CustomZombie(world1, p, pd.getBotspeed(), pd.getBothealth(), true);

		    ArenaManager ab = new ArenaManager();
		    ab.arenaManagerBot(p, zombie.getBukkitEntity());

		    new Inventories().hardcoreInventory(p);
		    pd.setGamemode(Gamemode.ONEVSONE);

		    p.sendMessage(Main.pre + "Your bot has been created!");
		    p.getWorld().playSound(p.getLocation(), Sound.CLICK, 100, 100);

		    e.setCancelled(true);
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + "Playerchecker")) {

		    new OverviewGui(p);
		    e.setCancelled(true);
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + "PlayerFreezer")) {

		    e.setCancelled(true);
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Buy a chest")) {

		    e.setCancelled(true);
		    p.performCommand("chest buy");
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Market")) {

		    if (pd.getGamemode() != Gamemode.MARKET)
			return;

		    e.setCancelled(true);
		    new MarketGui(p);
		}

		if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Sell abilities")) {

		    if (pd.getGamemode() != Gamemode.MARKET)
			return;

		    e.setCancelled(true);
		    new SellGui(p);
		}
	    }
	}
    }

    @EventHandler
    public void onFreeze(PlayerInteractEntityEvent e) {
	Player p = e.getPlayer();

	if (!(e.getRightClicked() instanceof Player))
	    return;

	Entity entity = e.getRightClicked();
	Player target = (Player) e.getRightClicked();

	if (!(entity instanceof Player)) { return; }

	if (!p.getItemInHand().hasItemMeta()) { return; }

	if (p.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + "PlayerFreezer")) {

	    p.performCommand("freeze " + target.getName());
	    e.setCancelled(true);
	}
    }
}
