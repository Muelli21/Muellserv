package me.Muell.server.trade;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.PlayerData;

public class TradeupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return false;

	Player p = (Player) sender;
	PlayerData pd = Main.getPlayerData(p);

	if (pd.getGamemode() != Gamemode.MARKET) {

	    p.sendMessage(ChatColor.RED + "You have to be in the abilityconfig area to use this command!");
	    return false;
	}

	new Tradeup().TradeUp(p);
	return false;
    }
}
