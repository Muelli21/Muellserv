package me.Muell.server.Guis;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionType;

import me.Muell.server.Main;
import me.Muell.server.playstyle.Playstyle;
import me.Muell.server.types.Items;
import me.Muell.server.types.PlayerData;

public class PlaystyleGui extends AbstractGui {

    public PlaystyleGui(Player p) {
	super(p, 27, "Playstyle", true);

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	setItemLeftAction(11, Items.createPotion(PotionType.INSTANT_HEAL, 1, "Pot", true), new AbstractAction() {

	    public void click(Player player) {

		PlayerData pd = Main.getPlayerData(p);

		pd.setPlaystyle(Playstyle.POT);
		p.closeInventory();
	    }
	});

	setItemLeftAction(13, Items.createitem(Material.MUSHROOM_SOUP, "Soup", null), new AbstractAction() {

	    public void click(Player player) {

		PlayerData pd = Main.getPlayerData(p);

		pd.setPlaystyle(Playstyle.SOUP);
		p.closeInventory();

	    }
	});

	setItemLeftAction(15, Items.createitem(Material.FISHING_ROD, "Rod", null), new AbstractAction() {

	    public void click(Player player) {

		PlayerData pd = Main.getPlayerData(p);

		pd.setPlaystyle(Playstyle.ROD);
		p.closeInventory();

	    }
	});

	p.openInventory(getInv());

    }

}
