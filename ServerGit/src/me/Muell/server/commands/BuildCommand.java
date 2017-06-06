package me.Muell.server.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.PlayerData;

public class BuildCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (sender.hasPermission("Head-Builder")) {

	    if (args.length == 1) {

		Player target = Bukkit.getPlayer(args[0]);

		if (target == null)
		    return false;

		PlayerData td = Main.getPlayerData(target);
		changeBuildStatus(td, sender);
	    }
	}

	if (sender.hasPermission("Builder")) {

	    if (args.length == 0) {

		if (!(sender instanceof Player)) {

		return false; }

		Player p = (Player) sender;
		PlayerData pd = Main.getPlayerData(p);

		changeBuildStatus(pd, sender);
	    }
	}

	return false;
    }

    public void changeBuildStatus(PlayerData pd, CommandSender sender) {

	String name = pd.getPlayer().getName();

	if (pd.isBuild()) {

	    pd.setBuild(false);
	    sender.sendMessage(ChatColor.RED + "Successfully disabled the build mode for player " + name);
	    return;
	}

	if (!pd.isBuild()) {

	    pd.setBuild(true);
	    sender.sendMessage(ChatColor.RED + "Successfully enabled the build mode for player " + name);
	    return;
	}
    }
}
