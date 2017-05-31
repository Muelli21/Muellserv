package me.Muell.server.abilities;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.Muell.server.Main;
import me.Muell.server.types.PlayerData;

public class AbilityCooldown {

    public boolean onCooldown(Player p, Ability ability, boolean message) {

	PlayerData pd = Main.getPlayerData(p);
	HashMap<Ability, Long> cooldown = pd.getAbilitycooldown();

	if (cooldown.get(ability) == null)
	    return false;

	if (cooldown.get(ability) > System.currentTimeMillis()) {

	    long seconds = (cooldown.get(ability) - System.currentTimeMillis()) / 1000;

	    if (message)
		p.sendMessage(ChatColor.DARK_PURPLE + ability.getName() + " is still on cooldown for " + seconds + " seconds!");

	    return true;

	}
	return false;
    }

    public void useAbility(Player p, Ability ability) {

	PlayerData pd = Main.getPlayerData(p);
	HashMap<Ability, Long> cooldown = pd.getAbilitycooldown();
	cooldown.put(ability, System.currentTimeMillis() + ability.getCooldown());
    }

}
