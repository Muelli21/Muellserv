package me.Muell.server.Guis;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.kitcreation.KitConfig;
import me.Muell.server.playstyle.PlayStyleInv;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.Items;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class KitChooseGui extends AbstractGui {

    public KitChooseGui(Player p) {
	super(p, 9, "Kits", false);

	PlayerData pd = Main.getPlayerData(p);

	if (pd.getGamemode() == Gamemode.MARKET)
	    setMarket(p);

	if (pd.getGamemode() != Gamemode.MARKET)
	    set(p);

	p.openInventory(getInv());
    }

    public void set(Player p) {

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	PlayerData pd = Main.getPlayerData(p);

	for (KitConfig config : pd.getKitconfigs().values()) {

	    ArrayList<String> abis = new ArrayList<>();
	    abis.add(ChatColor.GRAY + "Playstyle: " + config.getStyle().getName());
	    abis.add(ChatColor.GRAY + "Points: " + config.getPoints());

	    for (Ability abi : config.getAbilities())
		abis.add(ChatColor.DARK_GRAY + abi.getName());

	    setItemLeftAction(config.getSlot(), Items.createitem(Material.EMERALD_BLOCK, "KitConfig [" + config.getSlot() + "]: " + config.getName(), abis), new AbstractAction() {

		public void click(Player player) {

		    new Loader().setKit(p, config);
		    pd.setIngame(true);
		    delete();
		}
	    });

	}

	setItemLeftAction(8, Items.createitem(Material.REDSTONE_BLOCK, "Without Kit", Arrays.asList(ChatColor.WHITE + "Play oldschool", ChatColor.WHITE + "without a Kit")), new AbstractAction() {

	    public void click(Player player) {

		PlayStyleInv pinv = new PlayStyleInv();
		pinv.playStyleInv(p);
		pd.setIngame(true);
		delete();

	    }
	});

    }

    public void setMarket(Player p) {

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	setCloseable(true);

	PlayerData pd = Main.getPlayerData(p);

	for (KitConfig config : pd.getKitconfigs().values()) {

	    ArrayList<String> abis = new ArrayList<>();
	    abis.add(ChatColor.GRAY + "Playstyle: " + config.getStyle().getName());
	    abis.add(ChatColor.GRAY + "Points: " + config.getPoints());
	    for (Ability abi : config.getAbilities())
		abis.add(ChatColor.DARK_GRAY + abi.getName());

	    setItemLeftAction(config.getSlot(), Items.createitem(Material.EMERALD_BLOCK, "KitConfig [" + config.getSlot() + "]: " + config.getName(), abis), new AbstractAction() {

		public void click(Player player) {

		    new KitConfigGui(p, config);
		}
	    });
	}

    }
}
