package me.Muell.server.abilities;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.Muell.server.Main;
import me.Muell.server.types.BlockProcessor;

public class Prison {

    public static HashMap<Player, Prison> prisons = new HashMap<>();

    private Player p;
    private Entity target;
    int time;
    boolean joinable = false;
    private TargetEffect effect;
    private BukkitTask task;

    @SuppressWarnings("deprecation")
    public Prison(Player p, Entity entity, Material outermaterial, Material innermaterial, int time, TargetEffect targetEffect) {

	prisons.put(p, this);
	this.time = time;
	this.target = entity;
	this.p = p;
	this.setEffect(targetEffect);

	Location loc = entity.getLocation().clone().add(0, 3, 0);
	Location tploc = loc.clone().add(1, 1, 1);

	for (int x = 0; x < 3; x++) {
	    for (int z = 0; z < 3; z++) {
		for (int y = 0; y < 4; y++) {

		    if (x == 1 && z == 1 && (y == 1 || y == 2)) {

			Block block = entity.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
			Material blockmaterial = block.getType();
			byte data = block.getData();

			BlockProcessor.inst().add(block, innermaterial, (byte) 1, true);
			BlockProcessor.inst().addFuture(block, blockmaterial, data, time, true);

		    } else {

			Block block = entity.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
			Material blockmaterial = block.getType();
			byte data = block.getData();

			BlockProcessor.inst().add(block, outermaterial, (byte) 1, true);
			BlockProcessor.inst().addFuture(block, blockmaterial, data, time, true);
		    }
		}

	    }

	}

	entity.teleport(tploc);

	BukkitTask task = new BukkitRunnable() {

	    int time = 0;

	    @Override
	    public void run() {

		time++;
		joinable = true;

		if (target.isDead() || target == null) {
		    destroy();
		    cancel();
		    return;
		}

		if (time % 2 == 0 && targetEffect != null)
		    targetEffect.effect(target);

		if (time > getTime()) {
		    destroy();
		    cancel();
		    return;
		}

	    }
	}.runTaskTimer(Main.getPlugin(), 0, 1);

	this.task = task;
    }

    public interface TargetEffect {
	void effect(Entity target);

    }

    public void destroy() {

	prisons.remove(p);
	task.cancel();
    }

    public Entity getTarget() {
	return target;
    }

    public int getTime() {
	return time;
    }

    public Player getP() {
	return p;
    }

    public TargetEffect getEffect() {
	return effect;
    }

    public void setEffect(TargetEffect effect) {
	this.effect = effect;
    }

}
