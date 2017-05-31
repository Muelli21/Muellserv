package me.Muell.server.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;

public class CordsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	Player p = (Player) sender;
	Location loc = p.getLocation();

	if (!(sender instanceof Player)) { return true; }

	if (!p.hasPermission("administration")) {

	    p.sendMessage(Main.pre + Main.noperms);
	    return true;
	}

	if (args.length == 1) {

	    if (args[0].equalsIgnoreCase("1vs1")) {

		Main.getPlugin().getConfig().getStringList("1vs1");
		Main.getPlugin().getConfig().set("cords.1vs1.world", loc.getWorld().getName());
		Main.getPlugin().getConfig().set("cords.1vs1.x", loc.getX());
		Main.getPlugin().getConfig().set("cords.1vs1.y", loc.getY());
		Main.getPlugin().getConfig().set("cords.1vs1.z", loc.getZ());
		Main.getPlugin().getConfig().set("cords.1vs1.yaw", loc.getYaw());
		Main.getPlugin().getConfig().set("cords.1vs1.pitch", loc.getPitch());

		Main.getPlugin().saveConfig();

		p.sendMessage(Main.pre + ChatColor.GREEN + "setting 1vs1 cords!");

	    }

	    if (args[0].equalsIgnoreCase("hub")) {

		Main.getPlugin().getConfig().getStringList("hub");

		Main.getPlugin().getConfig().set("cords.hub.world", loc.getWorld().getName());
		Main.getPlugin().getConfig().set("cords.hub.x", loc.getX());
		Main.getPlugin().getConfig().set("cords.hub.y", loc.getY());
		Main.getPlugin().getConfig().set("cords.hub.z", loc.getZ());
		Main.getPlugin().getConfig().set("cords.hub.yaw", loc.getYaw());
		Main.getPlugin().getConfig().set("cords.hub.pitch", loc.getPitch());

		Main.getPlugin().saveConfig();

		p.sendMessage(Main.pre + ChatColor.GREEN + "Setting Hub!");

	    }

	    if (args[0].equalsIgnoreCase("ffa")) {

		Main.getPlugin().getConfig().getStringList("ffa");

		Main.getPlugin().getConfig().set("cords.ffa.world", loc.getWorld().getName());
		Main.getPlugin().getConfig().set("cords.ffa.x", loc.getX());
		Main.getPlugin().getConfig().set("cords.ffa.y", loc.getY());
		Main.getPlugin().getConfig().set("cords.ffa.z", loc.getZ());
		Main.getPlugin().getConfig().set("cords.ffa.yaw", loc.getYaw());
		Main.getPlugin().getConfig().set("cords.ffa.pitch", loc.getPitch());

		Main.getPlugin().saveConfig();

		p.sendMessage(Main.pre + ChatColor.GREEN + "Setting FFA cords!");

	    }

	    if (args[0].equalsIgnoreCase("classic")) {

		Main.getPlugin().getConfig().getStringList("classic");

		Main.getPlugin().getConfig().set("cords.classic.world", loc.getWorld().getName());
		Main.getPlugin().getConfig().set("cords.classic.x", loc.getX());
		Main.getPlugin().getConfig().set("cords.classic.y", loc.getY());
		Main.getPlugin().getConfig().set("cords.classic.z", loc.getZ());
		Main.getPlugin().getConfig().set("cords.classic.yaw", loc.getYaw());
		Main.getPlugin().getConfig().set("cords.classic.pitch", loc.getPitch());

		Main.getPlugin().saveConfig();

		p.sendMessage(Main.pre + ChatColor.GREEN + "Setting Classic cords!");

	    }

	    if (args[0].equalsIgnoreCase("kitcreation")) {

		Main.getPlugin().getConfig().getStringList("kitcreation");

		Main.getPlugin().getConfig().set("cords.kitcreation.world", loc.getWorld().getName());
		Main.getPlugin().getConfig().set("cords.kitcreation.x", loc.getX());
		Main.getPlugin().getConfig().set("cords.kitcreation.y", loc.getY());
		Main.getPlugin().getConfig().set("cords.kitcreation.z", loc.getZ());
		Main.getPlugin().getConfig().set("cords.kitcreation.yaw", loc.getYaw());
		Main.getPlugin().getConfig().set("cords.kitcreation.pitch", loc.getPitch());

		Main.getPlugin().saveConfig();

		p.sendMessage(Main.pre + ChatColor.GREEN + "Setting Kitcreation cords!");

	    }

	    if (args[0].equalsIgnoreCase("bot")) {

		Main.getPlugin().getConfig().getStringList("bot");

		Main.getPlugin().getConfig().set("cords.bot.world", loc.getWorld().getName());
		Main.getPlugin().getConfig().set("cords.bot.x", loc.getX());
		Main.getPlugin().getConfig().set("cords.bot.y", loc.getY());
		Main.getPlugin().getConfig().set("cords.bot.z", loc.getZ());
		Main.getPlugin().getConfig().set("cords.bot.yaw", loc.getYaw());
		Main.getPlugin().getConfig().set("cords.bot.pitch", loc.getPitch());

		Main.getPlugin().saveConfig();

		p.sendMessage(Main.pre + ChatColor.GREEN + "Setting Bot cords!");

	    }

	    if (args[0].equalsIgnoreCase("fps")) {

		Main.getPlugin().getConfig().getStringList("fps");

		Main.getPlugin().getConfig().set("cords.fps.world", loc.getWorld().getName());
		Main.getPlugin().getConfig().set("cords.fps.x", loc.getX());
		Main.getPlugin().getConfig().set("cords.fps.y", loc.getY());
		Main.getPlugin().getConfig().set("cords.fps.z", loc.getZ());
		Main.getPlugin().getConfig().set("cords.fps.yaw", loc.getYaw());
		Main.getPlugin().getConfig().set("cords.fps.pitch", loc.getPitch());

		Main.getPlugin().saveConfig();

		p.sendMessage(Main.pre + ChatColor.GREEN + "Setting Fps cords!");

	    }

	    if (args[0].equalsIgnoreCase("games")) {

		Main.getPlugin().getConfig().getStringList("games");

		Main.getPlugin().getConfig().set("cords.games.world", loc.getWorld().getName());
		Main.getPlugin().getConfig().set("cords.games.x", loc.getX());
		Main.getPlugin().getConfig().set("cords.games.y", loc.getY());
		Main.getPlugin().getConfig().set("cords.games.z", loc.getZ());
		Main.getPlugin().getConfig().set("cords.games.yaw", loc.getYaw());
		Main.getPlugin().getConfig().set("cords.games.pitch", loc.getPitch());

		Main.getPlugin().saveConfig();

		p.sendMessage(Main.pre + ChatColor.GREEN + "Setting games cords!");

	    }
	}

	else {

	    p.sendMessage(Main.pre + ChatColor.RED + "Use /cords [gamemodename]!");
	}

	return false;
    }

}
