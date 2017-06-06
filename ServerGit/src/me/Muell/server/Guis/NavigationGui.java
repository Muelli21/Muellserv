package me.Muell.server.Guis;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.abilities.AbilityChooseListener;
import me.Muell.server.playstyle.PlayStyleInv;
import me.Muell.server.types.DisguiseManager;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.Inventories;
import me.Muell.server.types.Items;
import me.Muell.server.types.PlayerData;
import me.Muell.server.types.Warps;

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

			p.teleport(Warps.ONEVSONE.getLocation());

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

		p.teleport(Warps.CLASSIC.getLocation());

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

			p.teleport(Warps.FFA.getLocation());

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

		p.teleport(Warps.FPS.getLocation());

		pd.setGamemode(Gamemode.FPS);

		PlayStyleInv ps = new PlayStyleInv();
		ps.playStyleInv(p);
		p.getInventory().setItem(8, Items.createitem(Material.STICK, ChatColor.RED + "Challenger", null));
	    }
	});

	setItemLeftAction(15, Items.createitem(Material.GOLDEN_CARROT, ChatColor.WHITE + "Oldschool",
		Arrays.asList(ChatColor.WHITE + "Fight all other players a little bit more oldschool, ", ChatColor.WHITE + "at this warp")), new AbstractAction() {

		    public void click(Player player) {

			PlayerData pd = Main.getPlayerData(p);

			pd.setGamemode(Gamemode.PVP);
			pd.setIngame(true);

			DisguiseManager disguise = new DisguiseManager();
			disguise.showCustom(p);

			p.closeInventory();
			p.teleport(Warps.OLDSCHOOL.getLocation());
			p.getInventory().clear();

			new Inventories().hardcoreInventory(p);
		    }
		});

	p.openInventory(getInv());

    }

}
