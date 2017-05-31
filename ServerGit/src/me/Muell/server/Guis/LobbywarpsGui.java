package me.Muell.server.Guis;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.DisguiseManager;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.Inventories;
import me.Muell.server.types.Items;
import me.Muell.server.types.PlayerData;

public class LobbywarpsGui extends AbstractGui {

    public LobbywarpsGui(Player p) {
	super(p, 27, "Lobbywarps", true);

	PlayerData pd = Main.getPlayerData(p);

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	setItemLeftAction(12, Items.createitem(Material.ANVIL, "Kitcreation", null), new AbstractAction() {

	    public void click(Player player) {

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
		delete();
	    }
	});

	setItemLeftAction(13, Items.createitem(Material.WORKBENCH, "Market", Arrays.asList(ChatColor.RED + "BETA!")), new AbstractAction() {

	    public void click(Player player) {

		Inventories inv = new Inventories();
		inv.MarketInventory(p);

		World world = Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.kitcreation.world"));
		double x = Main.getPlugin().getConfig().getDouble("cords.kitcreation.x");
		double y = Main.getPlugin().getConfig().getDouble("cords.kitcreation.y");
		double z = Main.getPlugin().getConfig().getDouble("cords.kitcreation.z");
		float yaw = (float) Main.getPlugin().getConfig().getDouble("cords.kitcreation.yaw");
		float pitch = (float) Main.getPlugin().getConfig().getDouble("cords.kitcreation.pitch");

		Location kitcreation = (new Location(world, x, y, z, yaw, pitch));
		p.teleport(kitcreation);
		pd.setGamemode(Gamemode.MARKET);
		delete();

	    }
	});
	setItemLeftAction(14, Items.createitem(Material.ENDER_CHEST, "Casino", Arrays.asList(ChatColor.RED + "Comings soon!")), null);
	p.openInventory(getInv());
    }

}
