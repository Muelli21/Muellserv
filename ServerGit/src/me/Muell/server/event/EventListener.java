package me.Muell.server.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

	Player p = e.getEntity();
	if (Games.allevents != null) {

	    for (Games games : Games.allevents) {

		if (games.getPlayers().contains(p)) {

		    games.removePlayer(p);
		}
	    }
	}
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

	Player p = e.getPlayer();
	if (Games.allevents != null) {

	    for (Games games : Games.allevents) {

		if (games.getPlayers().contains(p)) {

		    games.removePlayer(p);
		}
	    }
	}
    }

    public void alert(String type, double secondstostart) {

	Bukkit.broadcastMessage(ChatColor.YELLOW + "There is a " + type + " event starting in " + secondstostart + " seconds!");
    }

    public void startalert(String type, double secondstostart) {

	Bukkit.broadcastMessage("");
	Bukkit.broadcastMessage("~~~~~~~~~~~~~~~~~~~~~~~");
	Bukkit.broadcastMessage("");
	Bukkit.broadcastMessage(ChatColor.GOLD + "There is a " + type + " event starting in " + secondstostart + " seconds!");
	Bukkit.broadcastMessage(ChatColor.YELLOW + "The winner will get a great reward");
	Bukkit.broadcastMessage("");
	Bukkit.broadcastMessage("~~~~~~~~~~~~~~~~~~~~~~~");
	Bukkit.broadcastMessage("");
    }
}
