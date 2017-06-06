package me.Muell.server.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.Nick;

public class NickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return false;

	Player p = (Player) sender;

	if (!p.hasPermission("Staff")) {

	    p.sendMessage(Main.noperms);
	    return false;
	}

	if (args.length == 1) {

	    if (args[0].equals("off")) {

		if (Nick.nicks == null) { return false; }

		Nick nick = Nick.nicks.get(p);

		if (nick == null) { return false; }

		nick.nickoff(p);
		return false;

	    }

	    Nick nick = new Nick();
	    nick.nick(p, args[0]);
	}

	if (args.length == 2) {

	    if (Bukkit.getPlayer(args[0]) == null) {

		p.sendMessage(ChatColor.DARK_RED + "This Player is not online!");
		return false;
	    }

	    Player target = Bukkit.getPlayer(args[0]);

	    if (args[1].equals("off")) {

		Nick nick = Nick.nicks.get(p);
		nick.nickoff(target);
		return false;
	    }

	    Nick nick = new Nick();
	    nick.nick(target, args[1]);
	    return false;
	}
	return false;
    }
}
