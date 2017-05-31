package me.Muell.server.chest;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.PlayerData;

public class ChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return false;

	Player p = (Player) sender;
	PlayerData pd = Main.getPlayerData(p);

	if (args.length == 0) {

	    if (pd.getChests() < 1) {
		p.sendMessage(ChatColor.RED + "You do not have a chest to open!");
		return false;
	    }

	    pd.setChests(pd.getChests() - 1);
	    new Chest(p);
	    return false;
	}

	if (args.length == 1) {

	    if (args[0].equals("buy")) {

		if (pd.getPoints() - Chest.price >= 0) {

		    pd.setPoints(pd.getPoints() - Chest.price);
		    pd.setChests(pd.getChests() + 1);

		    p.sendMessage(ChatColor.GREEN + "Successfully bought a chest!");

		    return false;

		}

		p.sendMessage(ChatColor.RED + "You do not have enough money to buy a chest!");
		return false;

	    }

	}

	return false;
    }

}
