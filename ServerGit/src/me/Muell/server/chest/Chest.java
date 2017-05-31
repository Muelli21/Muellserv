package me.Muell.server.chest;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.types.Items;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class Chest {

    public static HashSet<Player> chestopening = new HashSet<>();
    public static int price = 1000;

    private Player p;
    private int speed = 100;
    private ArrayList<Ability> abilitiesinchest = new ArrayList<>();

    public Chest(Player p) {

	this.p = p;

	PlayerData pd = Main.getPlayerData(getP());

	Inventory inv = Bukkit.createInventory(null, 27, "Chest opening");

	for (int i = 0; i < inv.getSize(); i++) {

	    inv.setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null));
	}

	inv.setItem(4, Items.createitem(Material.STICK, "Chest", null));
	inv.setItem(22, Items.createitem(Material.STICK, "Chest", null));

	getP().openInventory(inv);
	p.updateInventory();

	chestopening.add(p);

	new BukkitRunnable() {

	    boolean loop = true;

	    @Override
	    public void run() {

		if (!p.isOnline())
		    return;

		if (loop) {
		    loop = false;

		    new BukkitRunnable() {

			@Override
			public void run() {

			    if (getSpeed() > 5) {
				setSpeed(getSpeed() - 1);
				getP().playSound(p.getLocation(), Sound.CLICK, 10, 10);

				Ability abi = new Loader().getrandomAbility();
				getChestAbilities().add(abi);

				if (getChestAbilities().size() >= 9)
				    getChestAbilities().remove(0);

				for (int i = 1; i < getChestAbilities().size(); i++)
				    inv.setItem(17 - i, getChestAbilities().get(i).getInvItem());

				p.updateInventory();
				loop = true;
				return;

			    }

			    if (getSpeed() <= 5) {
				ItemStack item = inv.getItem(13);
				Ability abi = new Loader().getAbility(item.getItemMeta().getDisplayName());

				pd.ownedAbilities.add(abi);
				getP().sendMessage(ChatColor.GOLD + "Congratulation, you won the Ability " + abi.getName());
				chestopening.remove(p);

				cancel();
				return;

			    }

			}
		    }.runTaskLater(Main.getPlugin(), 100 / getSpeed());

		}
	    }
	}.runTaskTimer(Main.getPlugin(), 20, 1);

    }

    public Player getP() {
	return p;
    }

    public void setP(Player p) {
	this.p = p;
    }

    public int getSpeed() {
	return speed;
    }

    public void setSpeed(int speed) {
	this.speed = speed;
    }

    public ArrayList<Ability> getChestAbilities() {
	return abilitiesinchest;
    }

}
