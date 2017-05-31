package me.Muell.server.playstyle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Guis.PlaystyleGui;

public class PlayStyleCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	Player p = (Player) sender;

	new PlaystyleGui(p);
	return false;
    }
}
