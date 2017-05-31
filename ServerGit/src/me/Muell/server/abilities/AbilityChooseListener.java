
package me.Muell.server.abilities;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;

import me.Muell.server.Main;
import me.Muell.server.Guis.KitChooseGui;
import me.Muell.server.playstyle.Playstyle;
import me.Muell.server.types.Items;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class AbilityChooseListener implements Listener {

    public void abilityChoosMode(Player p) {

	new KitChooseGui(p);
    }

    @EventHandler
    public void heal(PlayerPickupItemEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	ItemStack item = e.getItem().getItemStack();

	if (item.getType() == Material.MUSHROOM_SOUP) {

	    if (pd.getStyle() == Playstyle.POT) {

		ItemStack potion = Items.createPotion(PotionType.INSTANT_HEAL, 2, "Heal", true);
		item.setType(potion.getType());
		item.setData(potion.getData());
		item.setDurability(potion.getDurability());
	    }

	    if (pd.getStyle() == Playstyle.ROD) {

		Damageable d = p;
		p.setHealth(d.getHealth() + 4);
	    }
	}

    }

    @EventHandler
    public void onSwitch(PlayerItemHeldEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	final ItemStack item = p.getInventory().getContents()[e.getNewSlot()];

	if (item == null)
	    return;

	if (!item.hasItemMeta())
	    return;

	String abiname = item.getItemMeta().getDisplayName();
	String word = abiname.split(" ")[0];
	Ability abi = new Loader().getAbility(word);

	HashMap<Ability, Long> cooldown = pd.getAbilitycooldown();

	if (abi == null)
	    return;

	if (cooldown.get(abi) == null)
	    return;

	if (cooldown.get(abi) < System.currentTimeMillis()) {

	    ItemMeta meta = item.getItemMeta();
	    meta.setDisplayName(abi.getName());
	    item.setItemMeta(meta);
	    return;
	}

	long seconds = (cooldown.get(abi) - System.currentTimeMillis()) / 1000;

	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(abi.getName() + " [" + seconds + "] ");
	item.setItemMeta(meta);
    }

}
