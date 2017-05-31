package me.Muell.server.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.Hologram;

public class HologramCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return false;

	if (!(sender.hasPermission("Hologram"))) {

	    sender.sendMessage(ChatColor.RED + Main.noperms);
	    return false;
	}

	Player p = (Player) sender;

	if (args[0].equals("add")) {

	    String name = args[1];
	    String argss = Arrays.stream(args).collect(Collectors.joining(" "));
	    String message = argss.replace(args[0], "").replace(args[1], "").replace("&", "§");
	    Location loc = p.getEyeLocation();

	    if (Hologram.holograms.get(name) != null) {

		p.sendMessage(ChatColor.DARK_RED + "A hologram with this name exists already!");
		return false;
	    }

	    ArrayList<String> messages = new ArrayList<>();
	    messages.add(message);

	    Hologram hologram = new Hologram(loc, name);
	    hologram.set(messages);
	    hologram.show();

	    Main.getPlugin().getConfig().set("Hologram." + name + ".world", loc.getWorld().getName());
	    Main.getPlugin().getConfig().set("Hologram." + name + ".x", loc.getX());
	    Main.getPlugin().getConfig().set("Hologram." + name + ".y", loc.getY());
	    Main.getPlugin().getConfig().set("Hologram." + name + ".z", loc.getZ());
	    Main.getPlugin().getConfig().set("Hologram." + name + ".text", message);
	    Main.getPlugin().saveConfig();

	    p.sendMessage(ChatColor.GREEN + "Successfully added the Hologram " + name);
	}

	if (args[0].equals("remove")) {

	    String name = args[1];
	    Hologram hologram = Hologram.holograms.get(name);

	    if (hologram != null)
		hologram.destroy();

	    Main.getPlugin().getConfig().set("Hologram." + name, null);
	    Main.getPlugin().saveConfig();

	    p.sendMessage(ChatColor.DARK_GREEN + "Successfully removed the Hologram " + name);
	}

	if (args[0].equals("addline")) {

	    String name = args[1];
	    Hologram hologram = Hologram.holograms.get(name);

	    if (hologram == null)
		return false;

	    String argss = Arrays.stream(args).collect(Collectors.joining(" "));
	    String message = argss.replace(args[0], "").replace(args[1], "").replace("&", "§");

	    ArrayList<String> messages = hologram.getLinestext();
	    messages.add(message);

	    hologram.update(messages);

	    Main.getPlugin().getConfig().set("Hologram." + name + ".text", messages);
	    Main.getPlugin().saveConfig();
	}

	if (args[0].equals("list")) {

	    p.sendMessage(ChatColor.DARK_GREEN + "These are all holograms: ");

	    for (Hologram hologram : Hologram.allHolograms) {

		if (hologram.getName() == null)
		    return false;

		p.sendMessage(ChatColor.GREEN + hologram.getName());
	    }

	}

	return false;
    }

}
