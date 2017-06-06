
package me.Muell.server.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class RemoveCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!sender.hasPermission("Admin")) {

	    sender.sendMessage(Main.noperms);
	    return false;
	}

	if (args[0].equals("ability")) {

	    String name = args[1];
	    String abiname = args[2];

	    if (Bukkit.getOfflinePlayer(name) == null) {

		sender.sendMessage(ChatColor.RED + "This player has never been online on this server!");
		return false;

	    }

	    if (Main.getPlugin().getConfig().getString("Players." + Bukkit.getOfflinePlayer(name).getUniqueId().toString()) == null) {

		sender.sendMessage(ChatColor.RED + "This player does not exist in our data!");
		return false;

	    }

	    removeAbility(abiname, name, sender);
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
	    new Loader().changePoints(-points, target, sender, true);

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
	    new Loader().changeChests(-chests, target, sender, true);
	}

	return false;
    }

    @SuppressWarnings("deprecation")
    public void removeAbility(String abiname, String name, CommandSender sender) {

	if (Bukkit.getOfflinePlayer(name).isOnline()) {

	    Player target = Bukkit.getPlayer(name);
	    PlayerData td = Main.getPlayerData(target);

	    Loader loader = new Loader();

	    Ability ability = loader.getAbility(abiname);

	    if (td.ownedAbilities.contains(ability)) {

		loader.removeAbility(ability, target);

		target.sendMessage(ChatColor.GREEN + sender.getName() + " removed your ability " + ability.getName() + "!");
		sender.sendMessage(ChatColor.DARK_GREEN + "Successfully removed the ability " + ability.getName() + " from " + target.getName() + "!");

	    } else {

		sender.sendMessage(ChatColor.RED + "The player " + target.getName() + " does not own this ability!");
	    }

	}

	if (!Bukkit.getOfflinePlayer(name).isOnline()) {

	    OfflinePlayer target = Bukkit.getOfflinePlayer(name);

	    Ability ability = new Loader().getAbility(abiname);
	    new Loader().removeAbility(ability, target);

	    sender.sendMessage("Successfully removed the ability " + ability.getName() + "from the player " + target.getName());

	    return;

	}
    }

}
