package me.Muell.server.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Guis.OverviewGui;

public class OverviewCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return false;

	Player p = (Player) sender;

	if (!p.hasPermission("Admin"))
	    return false;

	if (args.length == 0) {

	    new OverviewGui(p);
	    return false;
	}

	return false;
    }

}
