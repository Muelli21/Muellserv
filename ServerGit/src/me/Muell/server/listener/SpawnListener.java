package me.Muell.server.listener;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Muell.server.Main;
import me.Muell.server.types.Hologram;
import me.Muell.server.types.Inventories;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class SpawnListener implements Listener {

    @EventHandler
    public void PlayerRespawn(PlayerRespawnEvent e) {

	final Player p = e.getPlayer();

	new BukkitRunnable() {

	    @Override
	    public void run() {

		spawnMethod(p);

		for (Hologram holograms : Hologram.allHolograms) {

		    holograms.showAfterDeath(p);
		}
	    }
	}.runTaskLater(Main.getPlugin(), 1);

    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
	Player p = e.getPlayer();

	new PlayerData(p);
	spawnMethod(p);

	Loader loader = new Loader();
	loader.LoadProfile(p);
    }

    public void spawnMethod(Player p) {

	World world = Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.hub.world"));
	double x = Main.getPlugin().getConfig().getDouble("cords.hub.x");
	double y = Main.getPlugin().getConfig().getDouble("cords.hub.y");
	double z = Main.getPlugin().getConfig().getDouble("cords.hub.z");
	float yaw = (float) Main.getPlugin().getConfig().getDouble("cords.hub.yaw");
	float pitch = (float) Main.getPlugin().getConfig().getDouble("cords.hub.pitch");

	Location spawn = (new Location(world, x, y, z, yaw, pitch));
	p.teleport(spawn);

	Inventories invs = new Inventories();

	invs.clearInventoy(p);
	invs.joinInventory(p);

	Loader loader = new Loader();
	loader.LoadHolograms(p);
	loader.clearPlayer(p);
    }
}
