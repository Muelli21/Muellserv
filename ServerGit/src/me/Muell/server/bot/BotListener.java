package me.Muell.server.bot;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class BotListener implements Listener {

    @EventHandler
    public void onEntityCombust(EntityCombustEvent e) {

	if (e.getEntity() instanceof Zombie) {

	    e.setCancelled(true);
	}
    }

    @EventHandler
    public void onCustomZombieDeath(EntityDeathEvent e) {

	if (e.getEntity() instanceof Zombie) {

	    e.setDroppedExp(0);
	    e.getDrops().clear();
	}
    }

    @EventHandler
    public void onRod(PlayerFishEvent e) {

	if (!(e.getCaught() instanceof Zombie)) { return; }

	Player p = e.getPlayer();
	Entity target = e.getCaught();

	double x = target.getLocation().getX();
	double y = target.getLocation().getY();
	double z = target.getLocation().getZ();

	double x1 = p.getLocation().getX();
	double y1 = p.getLocation().getY();
	double z1 = p.getLocation().getZ();

	Vector handle = new Vector((x) - (x1), (y) - (y1), (z) - (z1));
	handle.normalize().setY(0.18);

	target.setVelocity(handle);

    }
}
