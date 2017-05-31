package me.Muell.server.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerChangeListener implements Listener {

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent e) {

	if (!(e.getEntity() instanceof Player)) {

	    return;
	}

	e.setFoodLevel(20);
	e.setCancelled(true);

    }

}
