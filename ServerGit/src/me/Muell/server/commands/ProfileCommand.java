
package me.Muell.server.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.Guis.ProfileGui;

public class ProfileCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return false;

	if (!sender.hasPermission("Admin")) {

	    sender.sendMessage(Main.noperms);
	    return false;
	}

	if (args.length != 1)
	    return false;

	String name = args[0];

	if (Bukkit.getOfflinePlayer(name) == null) {

	    sender.sendMessage(ChatColor.RED + "This player has never been online on this server!");
	    return false;
	}

	if (Main.getPlugin().getConfig().getString("Players." + Bukkit.getOfflinePlayer(name).getUniqueId().toString()) == null) {

	    sender.sendMessage(ChatColor.RED + "This player does not exist in our data!");
	    return false;
	}

	OfflinePlayer target = Bukkit.getOfflinePlayer(name);
	Player p = (Player) sender;

	if (target.isOnline()) {
	    Player onlinetarget = (Player) target;
	    new ProfileGui(p, onlinetarget);
	}

	if (!target.isOnline()) {
	    new ProfileGui(p, target);
	}

	return false;
    }
}
