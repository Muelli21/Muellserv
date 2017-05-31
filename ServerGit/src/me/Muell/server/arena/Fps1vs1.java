package me.Muell.server.arena;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Muell.server.Main;
import me.Muell.server.playstyle.PlayStyleInv;
import me.Muell.server.types.DisguiseManager;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.Items;
import me.Muell.server.types.PlayerData;

public class Fps1vs1 {

    public void fps(Player p, Player target) {

	PlayerData pd = Main.getPlayerData(p);
	PlayerData td = Main.getPlayerData(target);

	PlayStyleInv pinv = new PlayStyleInv();
	pinv.playStyleInv(p);
	pinv.playStyleInv(target);

	pd.setIngame(true);
	td.setIngame(true);

	Location locp = p.getLocation();
	Location loctarget = target.getLocation();

	if (locp.distance(loctarget) < 10) {

	    Vector distancevector = new Vector(locp.getX() - loctarget.getX(), 0, locp.getZ() - loctarget.getZ());
	    distancevector.normalize();
	    distancevector.multiply(5);
	    distancevector.setY(0.5);

	    p.setVelocity(distancevector);
	    target.setVelocity(distancevector.multiply(-1));
	}

	DisguiseManager disguise = new DisguiseManager();

	disguise.hideallexcept(target, p);
	disguise.hideallexcept(p, target);

	new BukkitRunnable() {

	    @Override
	    public void run() {

		if (p.isDead()) {

		    target.sendMessage("Your opponent died!");
		    reset(target);

		    td.setIngame(false);

		    cancel();
		    return;

		} else

		if (pd.getGamemode() != Gamemode.FPS) {

		    target.sendMessage("Your opponent left the 1v1!");
		    reset(target);

		    pd.setIngame(false);
		    td.setIngame(false);

		    cancel();
		    return;

		} else

		if (target.isDead()) {

		    p.sendMessage("Your opponent died!");
		    reset(p);

		    pd.setIngame(false);
		    td.setIngame(false);

		    cancel();
		    return;

		} else

		if (td.getGamemode() != Gamemode.FPS) {

		    p.sendMessage("Your opponent left the 1v1!");
		    reset(p);

		    pd.setIngame(false);
		    td.setIngame(false);

		    cancel();
		    return;

		}

	    }
	}.runTaskTimer(Main.getPlugin(), 1L, 1L);

    }

    private void reset(Player p) {

	PlayStyleInv pinv = new PlayStyleInv();
	pinv.playStyleInv(p);
	p.getInventory().setItem(8, Items.createitem(Material.STICK, ChatColor.RED + "Challenger", null));

	DisguiseManager disguise = new DisguiseManager();
	disguise.showCustom(p);

    }
}
