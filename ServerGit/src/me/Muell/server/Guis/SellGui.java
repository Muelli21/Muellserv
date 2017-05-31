package me.Muell.server.Guis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.types.Items;
import me.Muell.server.types.Offer;
import me.Muell.server.types.PlayerData;

public class SellGui extends AbstractGui {

    private int site = 1;
    private int abilities = 1;
    private int price = 500;
    private HashMap<Integer, Ability> ability = new HashMap<>();

    public SellGui(Player p) {
	super(p, 54, "Sell an ability", true);

	setAbilities();
	p.openInventory(getInv());

    }

    public SellGui(Player p, Ability ability) {
	super(p, 9, "Ability: " + ability.getName(), true);

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	setItemLeftAction(0, Items.createDye(DyeColor.GREEN, "Sell this ability", null), new AbstractAction() {

	    public void click(Player player) {

		new Offer(p, ability, price);
		delete();
	    }
	});

	setPriceItem();

	setItemLeftAction(8, Items.createGlassPane(DyeColor.WHITE, "Back to all abilities", null), new AbstractAction() {

	    public void click(Player player) {

		new SellGui(p);

	    }
	});

	p.openInventory(getInv());

    }

    public void setPriceItem() {

	setItem(4, Items.createitem(Material.EMERALD, "Price: " + price, Arrays.asList(ChatColor.WHITE + "Left click to higher the price", ChatColor.WHITE + "Right click to lower it")),
		new AbstractAction() {

		    public void click(Player player) {

			setPrice(getPrice() + 50);
			setPriceItem();

		    }
		}, new AbstractAction() {

		    public void click(Player player) {

			if (getPrice() - 50 < 500)
			    return;

			setPrice(getPrice() - 50);
			setPriceItem();
		    }
		});
    }

    public void setAbilities() {

	clearGui();

	PlayerData pd = Main.getPlayerData(getP());

	clearGui();
	ability.clear();

	int amount = 0;
	for (Ability abi : pd.ownedAbilities) {

	    amount++;
	    ability.put(amount, abi);
	    abilities = 1;
	    site = 1;
	}

	showSite(getP());
    }

    public void showSite(Player p) {

	clearGui();
	for (int slot = 0; slot < 54; slot++) {

	    if (slot % 9 == 0) {
		setBackNext(p, slot);
		continue;
	    }

	    if ((slot + 1) % 9 == 0)
		continue;

	    addAbility(slot);
	    abilities++;
	}
    }

    public void setBackNext(Player p, int slot) {

	setItemLeftAction(slot, Items.createitem(Material.STAINED_GLASS_PANE, "Back", Arrays.asList(ChatColor.GRAY + "Go back")), new AbstractAction() {

	    public void click(Player player) {
		site--;
		setAbilities(getAbilities() - 84);

		if (site < 1) {
		    delete();
		    return;
		}

		showSite(p);
	    }
	});

	setItemLeftAction(slot + 8, Items.createitem(Material.STAINED_GLASS_PANE, "Next", Arrays.asList(ChatColor.GRAY + "Go to the next side")), new AbstractAction() {

	    public void click(Player player) {
		site++;
		showSite(p);
	    }
	});
    }

    public void addAbility(int slot) {

	Ability abi = ability.get(abilities);

	if (abi != null) {

	    ArrayList<String> lore = new ArrayList<>();
	    lore.add(ChatColor.GRAY + "Right click to sell this ability");

	    ItemStack item = abi.getInvItem().clone();
	    ItemMeta meta = item.getItemMeta();
	    meta.setLore(lore);
	    item.setItemMeta(meta);

	    setItem(slot, item, null, new AbstractAction() {

		public void click(Player player) {

		    new SellGui(getP(), abi);
		}
	    });

	} else {

	    setItem(slot, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

    }

    public int getSite() {
	return site;
    }

    public void setSite(int site) {
	this.site = site;
    }

    public int getAbilities() {
	return abilities;
    }

    public void setAbilities(int abilities) {
	this.abilities = abilities;
    }

    public int getPrice() {
	return price;
    }

    public void setPrice(int price) {
	this.price = price;
    }

}
