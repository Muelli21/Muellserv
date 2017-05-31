package me.Muell.server.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.Muell.server.Main;
import me.Muell.server.types.BlockProcessor;
import me.Muell.server.types.PlayerData;

public class BuildListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!pd.isBuild()) {

	    p.sendMessage(ChatColor.GRAY + "You can not build!");
	    e.setCancelled(true);
	}

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBreak(BlockBreakEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	Block block = e.getBlock();
	Material type = block.getType();
	byte data = block.getData();

	if (pd.isBuild())
	    return;

	if (pd.isIngame()) {

	    if (block.getType() == Material.BROWN_MUSHROOM || block.getType() == Material.RED_MUSHROOM) {

		block.breakNaturally();
		BlockProcessor.inst().addFuture(block, type, data, 2 * 20, true);
		return;
	    }

	    if (block.getType() == Material.LOG) {

		e.setCancelled(true);
		BlockProcessor.inst().add(block, Material.AIR, data, true);
		block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.BOWL, 3));
		BlockProcessor.inst().addFuture(block, type, data, 2 * 20, true);
		return;
	    }
	}

	e.setCancelled(true);
    }
}
