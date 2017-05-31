package me.Muell.server.types;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Playerchecks implements Listener {

    static final double ON_GROUND = -0.0784000015258789;

    public int ping(Player p) {
	CraftPlayer craftplayer = (CraftPlayer) p;
	int ping = craftplayer.getHandle().ping;
	return ping;
    }

    public boolean isFalling(Player p, PlayerMoveEvent e) {

	if (p.getVelocity().getY() < ON_GROUND)
	    return true;

	return false;

    }

    public boolean isOnground(Player p, PlayerMoveEvent e) {

	if (e.getFrom().getY() != e.getTo().getY())
	    return false;

	return true;

    }

    public boolean inAir(Player p) {

	if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR
		&& p.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType() == Material.AIR)
	    return true;

	return false;

    }

    public boolean isLiquid(Player p) {

	if (p.getLocation().subtract(0, 1, 0).getBlock().isLiquid())
	    return true;

	return false;

    }

    public boolean isInLiquid(Player p) {

	if (p.getLocation().getBlock().isLiquid())
	    return true;

	return false;

    }

    public boolean isIce(Player p, PlayerMoveEvent e) {

	if (e.getTo().clone().subtract(0, 1, 0).getBlock().getType() == (Material.ICE) || e.getTo().clone().subtract(0, 1, 0).getBlock().getType() == (Material.PACKED_ICE))
	    return true;

	return false;

    }

    public boolean isBlockabove(Player p, PlayerMoveEvent e) {

	if (p.getLocation().clone().add(0, 2, 0).getBlock().getType() != (Material.AIR))
	    return true;

	if (e.getFrom().clone().add(0, 2, 0).getBlock().getType() != (Material.AIR))
	    return true;

	return false;

    }

    public boolean isNormal(Player p, PlayerMoveEvent e) {

	if (e.getFrom().getBlock().isLiquid() || e.getFrom().getBlock().getRelative(BlockFace.DOWN).isLiquid() || e.getFrom().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)
	    return false;

	return true;

    }
}