package me.Muell.server.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.listener.SpawnListener;
import me.Muell.server.types.DisguiseManager;
import me.Muell.server.types.Inventories;
import me.Muell.server.types.PlayerData;

public class AdminCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	Player p = (Player) sender;
	PlayerData pd = Main.getPlayerData(p);

	if (!(sender instanceof Player)) { return false; }

	if (!p.hasPermission("Staff")) {

	    p.sendMessage(Main.pre + Main.noperms);
	    return true;
	}

	if (!(args.length == 0)) {

	    p.sendMessage(Main.pre + ChatColor.RED + "Use /admin!");
	    return true;
	}

	if (!pd.isInAdminmode()) {

	    Inventories invs = new Inventories();
	    invs.clearInventoy(p);
	    invs.adminInventoy(p);
	    pd.setAdminmode(true);
	    pd.setDisguised(true);

	    DisguiseManager disguise = new DisguiseManager();
	    disguise.disguise(p);

	    p.sendMessage(Main.pre + ChatColor.YELLOW + "You are in the adminmode!");

	} else {

	    Inventories invs = new Inventories();
	    invs.clearInventoy(p);

	    SpawnListener sl = new SpawnListener();
	    sl.spawnMethod(p);

	    p.sendMessage(Main.pre + ChatColor.YELLOW + "You left the adminmode! ");
	    p.sendMessage(Main.pre + ChatColor.RED + "Teleporting to Spawn!");
	}

	return false;
    }
}
