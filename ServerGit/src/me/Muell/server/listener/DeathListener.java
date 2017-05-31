package me.Muell.server.listener;

import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.playstyle.Playstyle;
import me.Muell.server.types.Gamemode;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class DeathListener implements Listener {

    @SuppressWarnings("unused")
    private Main main;

    public DeathListener(Main main) {

	this.main = main;

    }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent e) {

	Player p = (Player) e.getEntity();
	PlayerData pd = Main.getPlayerData(p);

	stuffCheck(e);

	if (e.getEntity().getKiller() == null)
	    return;

	e.setDeathMessage(null);

	Player target = e.getEntity().getKiller();

	Damageable d = target;

	p.sendMessage(ChatColor.WHITE + "Your opponent had " + (int) d.getHealth() / 2 + ChatColor.RED + " hearths" + ChatColor.WHITE + " left! ");

	refill(target);

	pd.setIngame(false);
    }

    private void stuffCheck(PlayerDeathEvent e) {

	ListIterator<ItemStack> litr = e.getDrops().listIterator();
	while (litr.hasNext()) {

	    ItemStack stack = litr.next();

	    if (stack.getType() == Material.MUSHROOM_SOUP)
		litr.remove();

	    if (stack.getType() == Material.BROWN_MUSHROOM)
		litr.remove();

	    if (stack.getType() == Material.RED_MUSHROOM)
		litr.remove();

	    if (stack.getType() == Material.BOWL)
		litr.remove();

	    if (stack.getType() == Material.POTION)
		litr.remove();

	    if (stack.getType() == Material.STONE_SWORD)
		litr.remove();

	    if (stack.getType() == Material.FISHING_ROD)
		litr.remove();

	    if (stack.getType() == Material.LEATHER_HELMET)
		litr.remove();

	    if (stack.getType() == Material.LEATHER_CHESTPLATE)
		litr.remove();

	    if (stack.getType() == Material.LEATHER_LEGGINGS)
		litr.remove();

	    if (stack.getType() == Material.LEATHER_BOOTS)
		litr.remove();

	    if (stack.getType() == Material.GOLDEN_CARROT)
		litr.remove();

	    if (stack.getType() == Material.ENDER_PEARL)
		litr.remove();

	    if (stack.hasItemMeta()) {

		String abiname = stack.getItemMeta().getDisplayName();
		String word = abiname.split(" ")[0];
		Ability abi = new Loader().getAbility(word);

		if (abi == null)
		    continue;

		litr.remove();
	    }
	}
    }

    private void refill(Player target) {

	PlayerData td = Main.getPlayerData(target);

	if (td.getGamemode() == Gamemode.PVP) {

	    if (td.getStyle().equals(Playstyle.SOUP))
		for (int i = 1; i <= 32; i++)
		    target.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));

	    if (td.getStyle().equals(Playstyle.POT))
		for (int i = 1; i <= 33; i++)
		    target.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 16421));

	    if (td.getStyle().equals(Playstyle.ROD))
		target.setHealth(240D);

	}
    }
}
