package me.Muell.server.trade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.PlayerData;

public class TradeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player)) { return false; }

	Player p = (Player) sender;
	PlayerData pd = Main.getPlayerData(p);

	if (pd.getGamemode() != Gamemode.MARKET) {

	    p.sendMessage(ChatColor.RED + "You have to be in the config and trading area to trade!");
	    return false;
	}

	if (args.length == 1) {

	    if (args[0].equalsIgnoreCase("accept")) {

		if (pd.getTradeinvite() == null || !pd.getTradeinvite().isOnline()) {

		    p.sendMessage(ChatColor.RED + "You have not got a trade invite yet!");
		    return false;
		}

		Player target = pd.getTradeinvite();

		TradeObject trade = new TradeObject(p, target);
		TradeObject partnertrade = new TradeObject(target, p);

		trade.setPartnertrade(partnertrade);
		partnertrade.setPartnertrade(trade);

		pd.setTradeinvite(null);
		return false;
	    }

	    if (Bukkit.getPlayer(args[0]) == null || !Bukkit.getPlayer(args[0]).isOnline()) {

		sender.sendMessage(ChatColor.RED + "This player does not exist or is not online!");
		return false;
	    }

	    Player target = Bukkit.getPlayer(args[0]);

	    if (target == p) {

		p.sendMessage(ChatColor.DARK_AQUA + "You can not trade with youself!");
		return false;
	    }

	    PlayerData td = Main.getPlayerData(target);

	    p.sendMessage(ChatColor.BLUE + "You send " + target.getName() + " a trade invite!");
	    target.sendMessage(ChatColor.BLUE + p.getName() + " send you a trade invite!");
	    td.setTradeinvite(p);
	    return false;
	}

	return false;
    }

}