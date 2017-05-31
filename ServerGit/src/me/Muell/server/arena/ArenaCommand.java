package me.Muell.server.arena;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;

public class ArenaCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	Player p = (Player) sender;
	Location loc = p.getLocation();

	if (!p.hasPermission("administration")) {

	    p.sendMessage(Main.pre + Main.noperms);
	    return false;
	}

	if (args.length != 2) {
	    p.sendMessage(ChatColor.RED + "Use /arena [name] spawn1/spawn2");
	    return false;
	}

	if (args[1].equalsIgnoreCase("spawn1")) {

	    Main.getPlugin().getConfig().get("Arena.name." + args[0]);
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn1.world", loc.getWorld().getName());
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn1.x", loc.getX());
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn1.y", loc.getY());
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn1.z", loc.getZ());
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn1.yaw", loc.getYaw());
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn1.pitch", loc.getPitch());
	    Main.getPlugin().saveConfig();

	    p.sendMessage(Main.pre + ChatColor.GREEN + "You have succsessfully set spawn1 for the Arena " + args[0]);

	} else

	if (args[1].equalsIgnoreCase("spawn2")) {

	    Main.getPlugin().getConfig().get("Arena.name." + args[0]);
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn2.world", loc.getWorld().getName());
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn2.x", loc.getX());
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn2.y", loc.getY());
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn2.z", loc.getZ());
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn2.yaw", loc.getYaw());
	    Main.getPlugin().getConfig().set("Arena.name." + args[0] + ".spawn2.pitch", loc.getPitch());
	    Main.getPlugin().saveConfig();

	    p.sendMessage(Main.pre + ChatColor.GREEN + "You have succsessfully set spawn2 for the Arena " + args[0]);

	} else {
	    p.sendMessage(ChatColor.RED + "Please use /arena [name] spawn1/spawn2!");
	}

	return false;
    }
}
