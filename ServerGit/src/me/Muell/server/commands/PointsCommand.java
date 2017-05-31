package me.Muell.server.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.PlayerData;

public class PointsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player)) {

	    sender.sendMessage(ChatColor.RED + "You can not use this command!");
	    return false;
	}

	Player p = (Player) sender;
	PlayerData pd = Main.getPlayerData(p);

	int points = pd.getPoints();
	p.sendMessage(ChatColor.GOLD + "Your current balance is " + points + " points!");
	return false;
    }

}
