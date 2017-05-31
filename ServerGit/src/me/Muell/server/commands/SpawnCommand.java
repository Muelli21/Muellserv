package me.Muell.server.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.listener.SpawnListener;
import me.Muell.server.types.PlayerData;

public class SpawnCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	Player p = (Player) sender;
	PlayerData pd = Main.getPlayerData(p);

	if (!(sender instanceof Player)) { return true; }

	if (args.length == 0) {

	    if (pd.isFrozen()) {

		p.sendMessage(Main.pre + ChatColor.BLUE + "You are frozen! You are not allowed to do this!");
		return true;
	    }

	    SpawnListener sl = new SpawnListener();
	    sl.spawnMethod(p);

	    p.sendMessage(Main.pre + ChatColor.RED + "Teleporting to the spawn!");
	}

	return false;
    }

}
