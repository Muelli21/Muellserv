package me.Muell.server.abilities;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Muell.server.Main;

public class Beam {

    public Beam(final Player p, final Material material, final Vector handle, boolean velocity, playerEffect effect) {

	new BukkitRunnable() {

	    Location loc = p.getEyeLocation().clone();

	    public void run() {

		loc.add(handle);

		if (loc.getBlock().getType().isSolid())
		    cancel();

		new BukkitRunnable() {

		    @Override
		    public void run() {

			cancel();

		    }
		}.runTaskLater(Main.getPlugin(), 7 * 20L);

		loc.getWorld().playEffect(loc, Effect.STEP_SOUND, material);

		for (Entity entity : loc.getWorld().getEntities()) {

		    if (!(entity instanceof LivingEntity))
			continue;

		    if (entity == p)
			continue;

		    if (entity.getLocation().distance(loc) > 1)
			continue;

		    effect.hit(entity);

		    if (velocity)
			entity.setVelocity(handle.normalize().setY(0.36));

		    cancel();
		}
	    }

	}.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    public interface playerEffect {
	void hit(Entity target);

    }

}
