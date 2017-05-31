package me.Muell.server.playstyle;

import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.Inventories;
import me.Muell.server.types.PlayerData;

public class PlayStyleInv {

    public void playStyleInv(Player p) {

	PlayerData pd = Main.getPlayerData(p);
	Inventories inv = new Inventories();

	if (pd.getStyle().equals(Playstyle.SOUP)) {

	    inv.ehgInventory(p);
	}

	if (pd.getStyle().equals(Playstyle.POT)) {

	    inv.potInventory(p);
	}

	if (pd.getStyle().equals(Playstyle.ROD)) {

	    inv.survivorInventory(p);
	}

    }

}
