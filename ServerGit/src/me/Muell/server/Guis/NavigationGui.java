package me.Muell.server.Guis;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.abilities.AbilityChooseListener;
import me.Muell.server.playstyle.PlayStyleInv;
import me.Muell.server.types.DisguiseManager;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.Inventories;
import me.Muell.server.types.Items;
import me.Muell.server.types.PlayerData;

public class NavigationGui extends AbstractGui {

    public NavigationGui(Player p) {
	super(p, 27, "Navigation", true);

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	setItemLeftAction(11, Items.createitem(Material.STICK, ChatColor.RED + "1vs1", Arrays.asList(ChatColor.WHITE + "Play some 1v1's and", ChatColor.WHITE + "fight against the bot!")),

		new AbstractAction() {

		    public void click(Player player) {

			DisguiseManager disguise = new DisguiseManager();
			disguise.showCustom(p);

			p.closeInventory();

			World world = Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.1vs1.world"));
			double x = Main.getPlugin().getConfig().getDouble("cords.1vs1.x");
			double y = Main.getPlugin().getConfig().getDouble("cords.1vs1.y");
			double z = Main.getPlugin().getConfig().getDouble("cords.1vs1.z");
			float yaw = (float) Main.getPlugin().getConfig().getDouble("cords.1vs1.yaw");
			float pitch = (float) Main.getPlugin().getConfig().getDouble("cords.1vs1.pitch");

			Location onevsone = (new Location(world, x, y, z, yaw, pitch));
			p.teleport(onevsone);

			Inventories inv = new Inventories();
			inv.onevsoneInventory(p);

		    }
		});

	setItemLeftAction(12, Items.createitem(Material.GRASS, ChatColor.GOLD + "Classic", Arrays.asList(ChatColor.WHITE + "A warp for oldschool-pvp")), new AbstractAction() {

	    public void click(Player player) {

		PlayerData pd = Main.getPlayerData(p);

		DisguiseManager disguise = new DisguiseManager();
		disguise.showCustom(p);

		p.closeInventory();

		World world = Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.classic.world"));
		double x = Main.getPlugin().getConfig().getDouble("cords.classic.x");
		double y = Main.getPlugin().getConfig().getDouble("cords.classic.y");
		double z = Main.getPlugin().getConfig().getDouble("cords.classic.z");
		float yaw = (float) Main.getPlugin().getConfig().getDouble("cords.classic.yaw");
		float pitch = (float) Main.getPlugin().getConfig().getDouble("cords.classic.pitch");

		Location classic = (new Location(world, x, y, z, yaw, pitch));

		p.teleport(classic);
		p.getInventory().clear();

		new PlayStyleInv().playStyleInv(p);

		pd.setIngame(true);
		pd.setGamemode(Gamemode.CLASSIC);
	    }
	});

	setItemLeftAction(13, Items.createitem(Material.STONE_SWORD, ChatColor.WHITE + "Pvp", Arrays.asList(ChatColor.WHITE + "Fight all other players, ", ChatColor.WHITE + "at this warp")),
		new AbstractAction() {

		    public void click(Player player) {

			PlayerData pd = Main.getPlayerData(p);

			pd.setGamemode(Gamemode.PVP);

			DisguiseManager disguise = new DisguiseManager();
			disguise.showCustom(p);

			p.closeInventory();

			World world = Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.ffa.world"));
			double x = Main.getPlugin().getConfig().getDouble("cords.ffa.x");
			double y = Main.getPlugin().getConfig().getDouble("cords.ffa.y");
			double z = Main.getPlugin().getConfig().getDouble("cords.ffa.z");
			float yaw = (float) Main.getPlugin().getConfig().getDouble("cords.ffa.yaw");
			float pitch = (float) Main.getPlugin().getConfig().getDouble("cords.ffa.pitch");

			Location ffa = (new Location(world, x, y, z, yaw, pitch));
			p.teleport(ffa);

			p.getInventory().clear();

			AbilityChooseListener abichoose = new AbilityChooseListener();
			abichoose.abilityChoosMode(p);

		    }
		});

	setItemLeftAction(14, Items.createitem(Material.STAINED_GLASS, ChatColor.GRAY + "FPS", Arrays.asList(ChatColor.WHITE + "Play over the clouds")), new AbstractAction() {

	    public void click(Player player) {

		PlayerData pd = Main.getPlayerData(p);

		DisguiseManager disguise = new DisguiseManager();
		disguise.showCustom(p);

		p.closeInventory();

		World world = Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.fps.world"));
		double x = Main.getPlugin().getConfig().getDouble("cords.fps.x");
		double y = Main.getPlugin().getConfig().getDouble("cords.fps.y");
		double z = Main.getPlugin().getConfig().getDouble("cords.fps.z");
		float yaw = (float) Main.getPlugin().getConfig().getDouble("cords.fps.yaw");
		float pitch = (float) Main.getPlugin().getConfig().getDouble("cords.fps.pitch");

		pd.setGamemode(Gamemode.FPS);

		Location fps = (new Location(world, x, y, z, yaw, pitch));
		p.teleport(fps);

		PlayStyleInv ps = new PlayStyleInv();
		ps.playStyleInv(p);
		p.getInventory().setItem(8, Items.createitem(Material.STICK, ChatColor.RED + "Challenger", null));
	    }
	});

	p.openInventory(getInv());

    }

}
