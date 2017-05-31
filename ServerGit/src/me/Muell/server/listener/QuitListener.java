package me.Muell.server.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Muell.server.Main;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

	Player p = e.getPlayer();

	new Loader().saveProfile(p);

	PlayerData pd = Main.getPlayerData(p);
	pd.delete();

    }

}
