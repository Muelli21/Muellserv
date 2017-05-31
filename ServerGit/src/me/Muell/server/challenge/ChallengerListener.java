package me.Muell.server.challenge;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Muell.server.Main;
import me.Muell.server.arena.ArenaManager;
import me.Muell.server.arena.Fps1vs1;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.PlayerData;

public class ChallengerListener implements Listener {

    @EventHandler
    public void onChallenge(PlayerInteractEntityEvent e) {

	if (e.getRightClicked() instanceof Player) {

	    final Player p = (Player) e.getPlayer();
	    final Player target = (Player) e.getRightClicked();

	    final PlayerData pd = Main.getPlayerData(p);
	    final PlayerData td = Main.getPlayerData(target);

	    if (!p.getItemInHand().hasItemMeta() || !p.getItemInHand().getType().equals(Material.STICK)
		    || !p.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Challenger")) { return; }

	    e.setCancelled(true);

	    if (!(pd.getDuels().contains(target.getName()))) {

		pd.duel(target);
		p.sendMessage(Main.pre + ChatColor.BLUE + "You challenged " + target.getName() + " !");
	    }

	    if (td.getDuels().contains(p.getName())) {

		if (pd.getGamemode() == Gamemode.FPS) {

		    new BukkitRunnable() {

			@Override
			public void run() {

			    pd.removeDuel(target.getName());
			    td.removeDuel(p.getName());
			    Fps1vs1 fps = new Fps1vs1();
			    fps.fps(p, target);

			}
		    }.runTask(Main.getPlugin());

		    return;
		}

		if (pd.getGamemode() != Gamemode.FPS) {

		    new BukkitRunnable() {

			@Override
			public void run() {

			    pd.removeDuel(target.getName());
			    td.removeDuel(p.getName());

			    ArenaManager.arenaManager(p, target);
			}
		    }.runTask(Main.getPlugin());

		    return;
		}

	    }
	}

    }
}
