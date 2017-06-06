package me.Muell.server.Guis;

import org.bukkit.DyeColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.Muell.server.types.Items;
import me.Muell.server.types.Loader;
import me.Muell.server.types.Rank;

public class RankGui extends AbstractGui {

    public RankGui(Player p, OfflinePlayer target) {
	super(p, 18, "Rank", true);

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	int slot = 1;
	for (Rank rank : Rank.values()) {

	    setItemLeftAction(slot, Items.createSkullHead((Player) target, rank.getPrefix() + rank.getName()), new AbstractAction() {

		public void click(Player player) {

		    new Loader().setRank(target, rank);
		    delete();
		}
	    });

	    slot++;
	}

	p.openInventory(getInv());
    }

}
