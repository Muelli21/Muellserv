
package me.Muell.server.event;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Muell.server.Main;
import me.Muell.server.abilities.AbilityChooseListener;
import me.Muell.server.listener.SpawnListener;
import me.Muell.server.playstyle.PlayStyleInv;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class Games {

    public static HashSet<Games> allevents = new HashSet<>();

    private HashSet<Player> players = new HashSet<>();
    private int playeramount;
    private int chestamount;
    private int pointamount;
    private boolean kits;

    public Games(int chestamount, int pointamount, boolean kits) {

	this.pointamount = pointamount;
	this.chestamount = chestamount;
	this.setKits(kits);
	allevents.add(this);
    }

    public HashSet<Player> getPlayers() {

	return players;
    }

    public void setPlayers(HashSet<Player> players) {

	this.players = players;
    }

    public int getPlayeramount() {

	return playeramount;
    }

    public void setPlayeramount(int playeramount) {

	this.playeramount = playeramount;
    }

    public int getChestamount() {

	return chestamount;
    }

    public void setChestamount(int chestamount) {

	this.chestamount = chestamount;
    }

    public boolean isKits() {

	return kits;
    }

    public void setKits(boolean kits) {

	this.kits = kits;
    }

    public int getPointamount() {

	return pointamount;
    }

    public void setPointamount(int pointamount) {

	this.pointamount = pointamount;
    }

    public void addPlayer(Player p) {

	this.players.add(p);
	this.playeramount++;
    }

    public void removePlayer(Player p) {

	this.players.remove(p);
	this.playeramount--;

	for (Player ps : players) {
	    ps.sendMessage(ChatColor.RED + "The player " + p.getName() + " resigned!");
	}

	if (playeramount == 1) {
	    win();

	    new BukkitRunnable() {

		@Override
		public void run() {

		    afterWin();
		}
	    }.runTaskLater(Main.getPlugin(), 5 * 20);
	}
    }

    public void start() {

	World world = Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.games.world"));
	double x = Main.getPlugin().getConfig().getDouble("cords.games.x");
	double y = Main.getPlugin().getConfig().getDouble("cords.games.y");
	double z = Main.getPlugin().getConfig().getDouble("cords.games.z");
	float yaw = (float) Main.getPlugin().getConfig().getDouble("cords.games.yaw");
	float pitch = (float) Main.getPlugin().getConfig().getDouble("cords.games.pitch");
	Location games = (new Location(world, x, y, z, yaw, pitch));

	for (Player ps : players) {

	    new Loader().clearPlayer(ps);
	    ps.sendMessage(ChatColor.YELLOW + "You have been teleported into the arena!");
	    ps.teleport(games);

	    if (isKits()) {
		AbilityChooseListener abichoose = new AbilityChooseListener();
		abichoose.abilityChoosMode(ps);
		continue;
	    }

	    new PlayStyleInv().playStyleInv(ps);
	}

	new BukkitRunnable() {

	    int time = 10;

	    @Override
	    public void run() {

		for (Player ps : players) {
		    ps.sendMessage(ChatColor.YELLOW + "The games will start in " + time + " seconds!");
		}

		time--;

		if (time < 1) {

		    if (players.size() == 0) {

			Bukkit.broadcastMessage(ChatColor.RED + "The event has been cancelled because nobody joined it! ");
			allevents.remove(this);
			cancel();
			return;
		    }

		    for (Player ps : players) {

			PlayerData pds = Main.getPlayerData(ps);

			pds.setIngame(true);
			ps.sendMessage(ChatColor.YELLOW + "The Games started");
		    }
		    cancel();
		    return;
		}
	    }
	}.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    public void startIn(long timeinseconds) {

	new BukkitRunnable() {

	    long time = timeinseconds;

	    @Override
	    public void run() {

		time--;

		if (time == 15 || time == 30 || time == 60) {
		    new EventListener().alert("games", time);
		}

		if (time < 5) {
		    new EventListener().alert("games", time);
		}

		if (time < 1) {

		    if (getPlayeramount() < 1) {

			Bukkit.broadcastMessage(ChatColor.RED + "The event has been cancelled because nobody joined it! ");
			cancel();
			return;
		    }

		    if (getPlayeramount() < 2) {

			time = 90;
			return;
		    }
		    start();
		    cancel();
		    return;
		}
	    }
	}.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    public void win() {

	for (Player ps : players) {

	    PlayerData pds = Main.getPlayerData(ps);
	    pds.setChests(pds.getChests() + chestamount);
	    pds.setPoints(pds.getPoints() + pointamount);
	    pds.setIngame(false);
	    ps.sendMessage(ChatColor.GOLD + "You won and earned " + chestamount + " chests and " + pointamount + " points!");
	}
    }

    public void afterWin() {

	for (Player ps : players)
	    new SpawnListener().spawnMethod(ps);

	allevents.remove(this);
    }
}
