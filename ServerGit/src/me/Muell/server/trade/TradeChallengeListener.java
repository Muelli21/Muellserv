package me.Muell.server.trade;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.Muell.server.Main;
import me.Muell.server.types.PlayerData;

public class TradeChallengeListener implements Listener {

    @EventHandler
    public void onChallenge(PlayerInteractEntityEvent e) {

	if (e.getRightClicked() instanceof Player) {

	    final Player p = (Player) e.getPlayer();
	    final Player target = (Player) e.getRightClicked();

	    final PlayerData pd = Main.getPlayerData(p);
	    final PlayerData td = Main.getPlayerData(target);

	    if (!p.getItemInHand().hasItemMeta()) { return; }

	    if (!p.getItemInHand().getType().equals(Material.STICK)) { return; }

	    if ((!p.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Trade"))) { return; }

	    e.setCancelled(true);

	    if (pd.getTradeinvite() == null || !pd.getTradeinvite().isOnline()) {

		p.sendMessage(ChatColor.BLUE + "You send " + target.getName() + " a trade invite!");
		target.sendMessage(ChatColor.BLUE + p.getName() + " send you a trade invite!");

		td.setTradeinvite(p);
		return;
	    }

	    Player tradeinvite = pd.getTradeinvite();

	    if (target == tradeinvite) {

		TradeObject trade = new TradeObject(p, target);
		TradeObject partnertrade = new TradeObject(target, p);

		trade.setPartnertrade(partnertrade);
		partnertrade.setPartnertrade(trade);

		pd.setTradeinvite(null);
		return;
	    }

	    if (target != tradeinvite) {
		p.sendMessage(ChatColor.BLUE + "You send " + target.getName() + " a trade invite!");
		target.sendMessage(ChatColor.BLUE + p.getName() + " send you a trade invite!");

		td.setTradeinvite(p);
		return;
	    }

	}

    }
}
