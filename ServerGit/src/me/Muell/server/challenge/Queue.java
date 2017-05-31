package me.Muell.server.challenge;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.Muell.server.arena.ArenaManager;
import me.Muell.server.types.Items;

public class Queue {

    public static ArrayList<Player> playersinqueue = new ArrayList<>();
    private Player p;

    public Queue(Player p) {

	this.p = p;

	if (playersinqueue.isEmpty()) {

	    playersinqueue.add(p);
	    p.sendMessage(ChatColor.GREEN + "Wait until another player joins the queue!");
	    queueInventory(p);
	    return;
	}

	for (Player inqueue : new ArrayList<>(playersinqueue)) {

	    if (inqueue == p)
		continue;

	    ArenaManager.arenaManager(p, inqueue);
	    playersinqueue.remove(inqueue);
	    break;
	}
    }

    public Player getP() {
	return p;
    }

    public void setP(Player p) {
	this.p = p;
    }

    private void queueInventory(Player p) {

	p.getInventory().clear();
	p.getInventory().setItem(4, Items.createitem(Material.ENDER_PEARL, "Leave the queue", null));
	p.updateInventory();
    }

}
