
package me.Muell.server.kitcreation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Muell.server.Main;
import me.Muell.server.Guis.KitChooseGui;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.PlayerData;

public class KitcreationListener implements Listener {

    @EventHandler
    public void onGuiOpen(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	Block block = e.getClickedBlock();

	if (block == null)
	    return;

	if (pd.getGamemode() != Gamemode.MARKET)
	    return;

	if (block.getType() == Material.ANVIL) {

	    e.setCancelled(true);
	    new KitChooseGui(p);

	}
    }

}
