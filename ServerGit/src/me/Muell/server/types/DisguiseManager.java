package me.Muell.server.types;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Muell.server.Main;

public class DisguiseManager {

    @SuppressWarnings("deprecation")
    public void showAll(Player p) {

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    p.showPlayer(ps);
	}
    }

    @SuppressWarnings("deprecation")
    public void showCustom(Player p) {
	for (Player ps : Bukkit.getOnlinePlayers()) {

	    PlayerData pds = Main.getPlayerData(ps);

	    if (p.hasPermission("Admin")) {
		p.showPlayer(ps);
		continue;
	    }

	    if (p.hasPermission("Moderator")) {

		if (ps.hasPermission("Admin") && pds.isDisguised())
		    continue;

		p.showPlayer(ps);
		continue;
	    }

	    if (p.hasPermission("TrialModerator")) {

		if (ps.hasPermission("Admin") && pds.isDisguised())
		    continue;

		if (ps.hasPermission("Moderator") && pds.isDisguised())
		    continue;

		p.showPlayer(ps);
		continue;
	    }

	    if (!p.hasPermission("TrialModerator")) {

		if (ps.hasPermission("Admin") && pds.isDisguised())
		    continue;

		if (ps.hasPermission("Moderator") && pds.isDisguised())
		    continue;

		if (ps.hasPermission("TrialModerator") && pds.isDisguised())
		    continue;

		p.showPlayer(ps);
		continue;
	    }
	}
    }

    @SuppressWarnings("deprecation")
    public void showtoAll(Player p) {

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    PlayerData pds = Main.getPlayerData(ps);

	    if (!pds.isAllowedtoseeplayers())
		continue;

	    ps.showPlayer(p);
	}
    }

    @SuppressWarnings("deprecation")
    public void hideall(Player p) {

	PlayerData pd = Main.getPlayerData(p);
	pd.setAllowedtoseeplayers(false);

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    p.hidePlayer(ps);
	}
    }

    @SuppressWarnings("deprecation")
    public void hideallexcept(Player players, Player p) {

	PlayerData pd = Main.getPlayerData(p);
	pd.setAllowedtoseeplayers(false);

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    if (ps == players)
		continue;

	    p.hidePlayer(ps);
	}
    }

    @SuppressWarnings("deprecation")
    public void hidefromallexcept(Player target, Player p) {

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    if (target == ps)
		continue;

	    ps.hidePlayer(p);
	}
    }

    @SuppressWarnings("deprecation")
    public void hidefromallexcept(HashSet<Player> players, Player p) {

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    if (players.contains(ps))
		continue;

	    ps.hidePlayer(p);
	}
    }

    @SuppressWarnings("deprecation")
    public void fix(Player p) {

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    if (p.canSee(ps)) {

		p.hidePlayer(ps);
		p.showPlayer(ps);
	    }
	}
    }

    @SuppressWarnings("deprecation")
    public void disguise(Player p) {

	PlayerData pd = Main.getPlayerData(p);

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    pd.setDisguised(true);

	    if (p.hasPermission("Admin")) {

		if (ps.hasPermission("Admin"))
		    continue;

		ps.hidePlayer(p);
		continue;
	    }

	    if (p.hasPermission("Moderator")) {

		if (ps.hasPermission("Admin"))
		    continue;

		if (ps.hasPermission("Moderator"))
		    continue;

		ps.hidePlayer(p);
		continue;
	    }

	    if (p.hasPermission("TrialModerator")) {

		if (ps.hasPermission("Admin"))
		    continue;

		if (ps.hasPermission("Moderator"))
		    continue;

		if (ps.hasPermission("TrialModerator"))
		    continue;

		ps.hidePlayer(p);
		continue;
	    }
	}
    }
}
