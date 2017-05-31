
package me.Muell.server.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.types.Loader;

public class DonateCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!sender.hasPermission("donate")) {

	    sender.sendMessage(Main.noperms);
	    return false;
	}

	Loader loader = new Loader();

	if (args[0].equals("ability")) {

	    String name = args[1];
	    String abiname = args[2];

	    Ability ability = new Loader().getAbility(abiname);

	    if (ability == null)
		return false;

	    if (Bukkit.getOfflinePlayer(name) == null) {

		sender.sendMessage(ChatColor.RED + "This player has never been online on this server!");
		return false;
	    }

	    if (Main.getPlugin().getConfig().getString("Players." + Bukkit.getOfflinePlayer(name).getUniqueId().toString()) == null) {

		sender.sendMessage(ChatColor.RED + "This player does not exist in our data!");
		return false;
	    }

	    loader.addAbility(ability, name, sender, true);

	}

	if (args[0].equals("points")) {

	    String name = args[1];
	    int points = Math.abs(Integer.parseInt(args[2]));

	    if (Bukkit.getOfflinePlayer(name) == null) {

		sender.sendMessage(ChatColor.RED + "This player has never been online on this server!");
		return false;

	    }

	    if (Main.getPlugin().getConfig().getString("Players." + Bukkit.getOfflinePlayer(name).getUniqueId().toString()) == null) {

		sender.sendMessage(ChatColor.RED + "This player does not exist in our data!");
		return false;

	    }

	    OfflinePlayer target = Bukkit.getOfflinePlayer(name);
	    loader.changePoints(points, target, sender, true);

	}

	if (args[0].equals("chests")) {

	    String name = args[1];
	    int chests = Math.abs(Integer.parseInt(args[2]));

	    if (Bukkit.getOfflinePlayer(name) == null) {

		sender.sendMessage(ChatColor.RED + "This player has never been online on this server!");
		return false;
	    }

	    if (Main.getPlugin().getConfig().getString("Players." + Bukkit.getOfflinePlayer(name).getUniqueId().toString()) == null) {

		sender.sendMessage(ChatColor.RED + "This player does not exist in our data!");
		return false;
	    }

	    OfflinePlayer target = Bukkit.getOfflinePlayer(name);
	    loader.changeChests(chests, target, sender, true);
	}

	return false;
    }

}
