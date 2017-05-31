package me.Muell.server.Guis;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.Items;

public class OverviewGui extends AbstractGui {

    private HashMap<Integer, Player> players = new HashMap<>();
    private int site = 1;
    private int player = 1;

    @SuppressWarnings("deprecation")
    public OverviewGui(Player p) {
	super(p, 54, "Online players", true);

	int amount = 0;
	for (Player ps : Bukkit.getOnlinePlayers()) {
	    amount++;
	    players.put(amount, ps);
	}
	showSite();
	p.openInventory(getInv());
    }

    public void showSite() {

	clearGui();
	for (int slot = 0; slot < 54; slot++) {

	    if (slot % 9 == 0) {
		setBackNext(slot);
		continue;
	    }

	    if ((slot + 1) % 9 == 0)
		continue;

	    addPlayer(slot);
	    player++;
	}
    }

    public void addPlayer(int slot) {

	Player ps = players.get(player);

	if (ps != null) {

	    setItem(slot, Items.createSkullHead(ps, ps.getName()), new AbstractAction() {

		public void click(Player player) {

		    getP().teleport(ps);
		    getP().sendMessage(Main.pre + ChatColor.GREEN + "Teleporting to " + ps.getName());
		    getP().closeInventory();
		}
	    }, new AbstractAction() {

		public void click(Player player) {
		    new ProfileGui(getP(), ps);
		}
	    });
	} else {

	    setItem(slot, Items.createGlassPane(DyeColor.GRAY, "", null), null, null);
	}

    }

    public void setBackNext(int slot) {

	setItemLeftAction(slot, Items.createitem(Material.STAINED_GLASS_PANE, "Back", Arrays.asList(ChatColor.WHITE + "Go back")), new AbstractAction() {

	    public void click(Player player) {
		site--;
		setPlayer(getPlayer() - 84);

		if (site < 1) {
		    getP().closeInventory();
		    delete();
		    return;
		}

		showSite();
	    }
	});

	setItemLeftAction(slot + 8, Items.createitem(Material.STAINED_GLASS_PANE, "Next", Arrays.asList(ChatColor.WHITE + "Go to the next side")), new AbstractAction() {

	    public void click(Player player) {
		site++;
		showSite();
	    }
	});
    }

    public int getSite() {
	return site;
    }

    public void setSite(int site) {
	this.site = site;
    }

    public int getPlayer() {
	return player;
    }

    public void setPlayer(int player) {
	this.player = player;
    }

}
