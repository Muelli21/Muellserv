package me.Muell.server.Guis;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.Muell.server.event.Games;
import me.Muell.server.types.Items;

public class GamesGui extends AbstractGui {

    private int points = 1000;
    private int chests = 1;
    private int timetostart = 60;
    private boolean kits = true;

    public GamesGui(Player p) {
	super(p, 9, "Games", true);

	set();
	p.openInventory(getInv());
    }

    public void set() {

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	setPointsItem();
	setChestsItem();
	setKitsItem();
	setTimeItem();

	setItemLeftAction(8, Items.createDye(DyeColor.GREEN, "start", null), new AbstractAction() {

	    public void click(Player player) {

		Games games = new Games(chests, points, kits);
		games.startIn(timetostart);
		getP().closeInventory();
	    }
	});

    }

    public void setPointsItem() {

	setItem(0, Items.createitem(Material.EMERALD, "Points", Arrays.asList(ChatColor.WHITE + "" + points)), new AbstractAction() {

	    public void click(Player player) {

		setPoints(getPoints() + 100);
		setPointsItem();
	    }
	}, new AbstractAction() {

	    public void click(Player player) {

		if (getPoints() - 100 < 0) {
		    setPoints(0);
		    setPointsItem();
		    return;
		}

		setPoints(getPoints() - 100);
		setPointsItem();
	    }
	});

    }

    public void setChestsItem() {

	setItem(1, Items.createitem(Material.CHEST, "Chests", Arrays.asList(ChatColor.WHITE + "" + chests)), new AbstractAction() {

	    public void click(Player player) {

		setChests(getChests() + 1);
		setChestsItem();
	    }
	}, new AbstractAction() {

	    public void click(Player player) {

		if (getChests() - 1 < 0) {
		    setChests(0);
		    setChestsItem();
		    return;
		}

		setChests(getChests() - 1);
		setChestsItem();
	    }
	});
    }

    public void setKitsItem() {

	setItemLeftAction(2, Items.createitem(Material.WORKBENCH, "Kits allowed: " + isKits(), Arrays.asList(ChatColor.WHITE + "allow or disallow the usage of kits")), new AbstractAction() {

	    public void click(Player player) {

		if (isKits())
		    setKits(false);

		else if (!isKits())
		    setKits(true);

		setKitsItem();
	    }
	});

    }

    public void setTimeItem() {

	setItem(3, Items.createitem(Material.WATCH, "Time until start", Arrays.asList(ChatColor.WHITE + "" + timetostart)), new AbstractAction() {

	    public void click(Player player) {

		setTimetostart(getTimetostart() + 5);
		setTimeItem();
	    }
	}, new AbstractAction() {

	    public void click(Player player) {

		if (getTimetostart() - 5 < 15) {
		    setTimetostart(15);
		    setTimeItem();
		    return;
		}

		setTimetostart(getTimetostart() - 5);
		setTimeItem();
	    }
	});
    }

    public int getPoints() {
	return points;
    }

    public void setPoints(int points) {
	this.points = points;
    }

    public int getChests() {
	return chests;
    }

    public void setChests(int chests) {
	this.chests = chests;
    }

    public boolean isKits() {
	return kits;
    }

    public void setKits(boolean kits) {
	this.kits = kits;
    }

    public int getTimetostart() {
	return timetostart;
    }

    public void setTimetostart(int timetostart) {
	this.timetostart = timetostart;
    }

}
