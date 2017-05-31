package me.Muell.server.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.Muell.server.Main;
import me.Muell.server.types.PlayerData;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {

	if (e.getEntity() instanceof Player) {

	    Player p = (Player) e.getEntity();

	    PlayerData pd = Main.getPlayerData(p);

	    if (!pd.isIngame()) {

		e.setCancelled(true);
	    }

	}
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {

	if (!(e.getEntity() instanceof Player)) { return;

	}

	if (!(e.getDamager() instanceof Player)) { return;

	}

	Player p = (Player) e.getDamager();
	Player target = (Player) e.getEntity();

	PlayerData pd = Main.getPlayerData(p);
	PlayerData td = Main.getPlayerData(target);

	if (pd.isInAdminmode()) {

	return; }

	if (td.isInAdminmode()) {

	return; }

	if (!pd.isIngame()) {

	    e.setCancelled(true);
	}

	if (!td.isIngame()) {

	    e.setCancelled(true);
	}

    }

}
