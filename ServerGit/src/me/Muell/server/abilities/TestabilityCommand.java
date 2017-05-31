package me.Muell.server.abilities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.Guis.TestAbilityGui;

public class TestabilityCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return false;

	Player p = (Player) sender;

	if (!p.hasPermission("administration")) {
	    p.sendMessage(Main.noperms);
	    return false;
	}

	if (args.length >= 1) {
	    p.sendMessage(ChatColor.RED + "Use /testability");
	    return false;
	}

	new TestAbilityGui(p);
	return false;
    }

}
