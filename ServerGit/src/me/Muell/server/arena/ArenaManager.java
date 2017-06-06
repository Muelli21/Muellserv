package me.Muell.server.arena;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.Muell.server.Main;
import me.Muell.server.abilities.AbilityChooseListener;
import me.Muell.server.listener.SpawnListener;
import me.Muell.server.playstyle.PlayStyleInv;
import me.Muell.server.types.DisguiseManager;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.PlayerData;

public class ArenaManager implements Listener {

    public static void arenaManager(final Player p, final Player target) {

	final PlayerData pd = Main.getPlayerData(p);
	final PlayerData td = Main.getPlayerData(target);

	if (Arena.list.isEmpty()) {
	    new SpawnListener().spawnMethod(p);
	    new SpawnListener().spawnMethod(target);
	    p.sendMessage("No Arena found");
	    target.sendMessage("No Arena found");
	    return;
	}

	for (final Arena arena : Arena.list) {

	    if (arena.isFree()) {

		arena.setFree(false);
		arena.setP1(p);
		arena.setP1(target);

		p.getInventory().clear();

		PlayStyleInv ps = new PlayStyleInv();
		ps.playStyleInv(p);
		ps.playStyleInv(target);

		p.teleport(arena.getSpawn2());
		target.teleport(arena.getSpawn1());

		DisguiseManager disguise = new DisguiseManager();
		disguise.showCustom(p);

		pd.setCurrentArena(arena);
		pd.setGamemode(Gamemode.ONEVSONE);
		pd.setIngame(true);

		td.setCurrentArena(arena);
		td.setGamemode(Gamemode.ONEVSONE);
		td.setIngame(true);

		AbilityChooseListener abichoose = new AbilityChooseListener();
		abichoose.abilityChoosMode(p);
		abichoose.abilityChoosMode(target);

		new BukkitRunnable() {

		    @Override
		    public void run() {

			if (p.isDead()) {

			    target.sendMessage("Your opponent died!");

			    pd.setGamemode(Gamemode.SPAWN);
			    pd.setIngame(false);
			    pd.setCurrentArena(null);

			    td.setGamemode(Gamemode.SPAWN);
			    td.setIngame(false);
			    td.setCurrentArena(null);

			    SpawnListener sl = new SpawnListener();
			    sl.spawnMethod(target);

			    arena.setFree(true);
			    arena.setP1(null);
			    arena.setP2(null);

			    cancel();
			    return;

			} else

			if (pd.getGamemode() != Gamemode.ONEVSONE) {

			    target.sendMessage("Your opponent left the 1v1!");

			    pd.setGamemode(Gamemode.SPAWN);
			    pd.setIngame(false);
			    pd.setCurrentArena(null);

			    td.setGamemode(Gamemode.SPAWN);
			    td.setIngame(false);
			    td.setCurrentArena(null);

			    SpawnListener sl = new SpawnListener();
			    sl.spawnMethod(target);

			    arena.setFree(true);
			    arena.setP1(null);
			    arena.setP2(null);

			    cancel();
			    return;

			} else

			if (target.isDead()) {

			    p.sendMessage("Your opponent died!");

			    pd.setGamemode(Gamemode.SPAWN);
			    pd.setIngame(false);
			    pd.setCurrentArena(null);

			    td.setGamemode(Gamemode.SPAWN);
			    td.setIngame(false);
			    td.setCurrentArena(null);

			    SpawnListener sl = new SpawnListener();
			    sl.spawnMethod(p);

			    arena.setFree(true);
			    arena.setP1(null);
			    arena.setP2(null);

			    cancel();
			    return;

			} else

			if (td.getGamemode() != Gamemode.ONEVSONE) {

			    p.sendMessage("Your opponent left the 1v1!");

			    pd.setGamemode(Gamemode.SPAWN);
			    pd.setIngame(false);
			    pd.setCurrentArena(null);

			    td.setGamemode(Gamemode.SPAWN);
			    td.setIngame(false);
			    td.setCurrentArena(null);

			    SpawnListener sl = new SpawnListener();
			    sl.spawnMethod(p);

			    arena.setFree(true);
			    arena.setP1(null);
			    arena.setP2(null);

			    cancel();
			    return;

			}

		    }
		}.runTaskTimer(Main.getPlugin(), 1L, 1L);

		break;

	    } else {
		p.sendMessage("No free arena found!");
		target.sendMessage("No free arena found!");

	    }

	}

    }

    public void arenaManagerBot(final Player p, final Entity entity) {

	final PlayerData pd = Main.getPlayerData(p);

	if (Arena.list.isEmpty()) {
	    new SpawnListener().spawnMethod(p);
	    p.sendMessage("No Arena found");
	    return;
	}

	for (final Arena arena : Arena.list) {

	    if (arena.isFree()) {

		arena.setFree(false);
		arena.setP1(p);
		arena.setBot(entity);

		p.teleport(arena.getSpawn1());
		entity.teleport(arena.getSpawn2());

		pd.setCurrentArena(arena);
		pd.setGamemode(Gamemode.ONEVSONE);
		pd.setIngame(true);

		new BukkitRunnable() {

		    @Override
		    public void run() {
			if (p.isDead()) {

			    pd.setGamemode(Gamemode.SPAWN);
			    pd.setIngame(false);
			    pd.setCurrentArena(null);

			    arena.setFree(true);

			    cancel();
			    return;

			} else

			if (pd.getGamemode() != Gamemode.ONEVSONE) {

			    pd.setGamemode(Gamemode.SPAWN);
			    pd.setIngame(false);
			    pd.setCurrentArena(null);

			    arena.setFree(true);

			    cancel();
			    return;

			}

		    }
		}.runTaskTimer(Main.getPlugin(), 1L, 1L);

		break;
	    } else {
		p.sendMessage("No free arena found!");

	    }
	}
    }

}
