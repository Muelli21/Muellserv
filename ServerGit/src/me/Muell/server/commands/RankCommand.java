package me.Muell.server.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.Guis.RankGui;
import me.Muell.server.types.Rank;

public class RankCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {

	if (!(sender instanceof Player))
	    return false;

	Player p = (Player) sender;

	if (args.length == 1) {

	    if (sender.hasPermission("Admin")) {

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

		if (target == null) {
		    sender.sendMessage(Main.pre + "This player does not exist");
		    return false;
		}

		new RankGui(p, target);
	    }
	}

	if (args.length == 0) {

	    sender.sendMessage("----------------------------------");
	    sender.sendMessage("These are all ranks on this server");
	    sender.sendMessage("----------------------------------");

	    for (Rank rank : Rank.values())
		sender.sendMessage(rank.getPrefix() + rank.getName());
	}

	return false;
    }

}
