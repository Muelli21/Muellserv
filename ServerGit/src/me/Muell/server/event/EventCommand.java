package me.Muell.server.event;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Guis.GamesGui;

public class EventCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (sender instanceof Player) {

	    Player p = (Player) sender;

	    if (args.length == 1) {

		if (Games.allevents == null) { return false; }

		if (args[0].equals("join")) {

		    if (Games.allevents.isEmpty() || Games.allevents == null) {

			sender.sendMessage(ChatColor.RED + "There is no games event to join!");
			return false;
		    }

		    for (Games games : Games.allevents) {

			if (games.getPlayers().contains(p))
			    return false;

			games.addPlayer(p);
			p.sendMessage(ChatColor.YELLOW + "You joined an event!");
			return false;
		    }
		}

		if (args[0].equals("list")) {

		    for (Games games : Games.allevents) {

			if (games.getPlayers().contains(p)) {

			    p.sendMessage(ChatColor.GOLD + "These are the players of the event!");

			    for (Player ps : games.getPlayers())
				p.sendMessage(ps.getName());

			    p.sendMessage(ChatColor.GOLD + "Players: " + games.getPlayeramount());
			}
		    }
		}

		if (args[0].equals("leave")) {

		    for (Games games : Games.allevents) {

			if (games.getPlayers().contains(p)) {

			    games.removePlayer(p);
			    p.sendMessage(ChatColor.YELLOW + "You left the event!");
			    return false;
			}
		    }
		}

	    } else {

		sender.sendMessage(ChatColor.RED + "Use /event [type] leave/list/join!");
	    }
	}

	if (sender.hasPermission("event")) {

	    if (args.length == 0)
		return false;

	    if (args[0].equals("games")) {

		if (Games.allevents.size() == 0) {

		    if (args.length == 4) {

			Games games = new Games(Integer.parseInt(args[1]), Integer.parseInt(args[2]), true);
			games.startIn(Long.parseLong(args[3]));
			sender.sendMessage(ChatColor.GREEN + "Successfully created a games event!");
			new EventListener().startalert("games", Long.parseLong(args[3]));
		    }

		    if (args.length == 1) {

			if (!(sender instanceof Player))
			    return false;

			Player p = (Player) sender;
			new GamesGui(p);
		    }
		}
	    }
	}
	return false;
    }
}
