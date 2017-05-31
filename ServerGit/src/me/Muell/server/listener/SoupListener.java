package me.Muell.server.listener;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SoupListener implements Listener {

    @EventHandler
    public void OnSoup(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	Damageable d = p;

	if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

	    if (p.getItemInHand().getType().equals(Material.MUSHROOM_SOUP)) {

		if ((d.getHealth() < 20) && (d.getHealth() > 0)) {

		    if (d.getHealth() <= 13) {

			p.getItemInHand().setType(Material.BOWL);
			p.setHealth(d.getHealth() + 7);

		    } else

		    if ((d.getHealth() <= 20) && (d.getHealth() > 20 - 7)) {

			p.getItemInHand().setType(Material.BOWL);
			p.setHealth(20D);

		    }

		} else if (p.getFoodLevel() < 20) {

		    if (p.getFoodLevel() <= 13) {

			p.getItemInHand().setType(Material.BOWL);
			p.setFoodLevel(p.getFoodLevel() + 7);

		    }

		    if (p.getFoodLevel() <= 20) {

			p.getItemInHand().setType(Material.BOWL);
			p.setFoodLevel(20);

		    }

		}

	    }

	}
    }

}
