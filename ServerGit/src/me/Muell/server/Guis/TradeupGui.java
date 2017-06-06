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
import me.Muell.server.chest.Chest;
import me.Muell.server.types.Items;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class TradeupGui extends AbstractGui {

    private int site = 1;
    private int abilities = 1;
    private int abilitypoints = 0;
    private HashMap<Integer, Ability> ability = new HashMap<>();
    private ArrayList<Ability> tradeupabilities = new ArrayList<>();

    public TradeupGui(Player p) {
	super(p, 54, "TradeUp", true);
	set();
	p.openInventory(getInv());
    }

    private void set() {

	clearGui();

	for (int i = 0; i < getInv().getSize(); i++)
	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);

	setItemLeftAction(0, Items.createitem(Material.CHEST, "Add an ability", null), new AbstractAction() {

	    public void click(Player player) {

		setAbilities();
	    }
	});

	setItemLeftAction(1, Items.createitem(Material.EMERALD, getAbilitypoints() + "/50 points to earn a chest", null), null);

	int slot = 2;
	for (Ability ability : getTradeupabilities()) {

	    setItemRightAction(slot,
		    Items.createitem(ability.getInvItem().getType(), ability.getInvItem().getItemMeta().getDisplayName(), Arrays.asList(ChatColor.WHITE + "Right click to remove from tradeup")),
		    new AbstractAction() {

			public void click(Player player) {

			    tradeupabilities.remove(ability);
			    setAbilitypoints(getAbilitypoints() - ability.getKitpoints());
			    set();
			}
		    });

	    slot++;
	}

	setItemLeftAction(53, Items.createitem(Material.STAINED_GLASS_PANE, "Back", null), new AbstractAction() {

	    public void click(Player player) {

		delete();
		return;
	    }

	});

    }

    public void setAbilities() {

	clearGui();

	PlayerData pd = Main.getPlayerData(getP());
	ability.clear();

	ArrayList<Ability> realabis = new ArrayList<>(pd.ownedAbilities);

	for (Ability abi : tradeupabilities)
	    realabis.remove(abi);

	int amount = 0;
	for (Ability abi : realabis) {

	    amount++;
	    ability.put(amount, abi);
	    abilities = 1;
	    site = 1;
	}

	showSite();
    }

    public void showSite() {

	clearGui();
	for (int slot = 0; slot < 54; slot++) {

	    if (slot % 9 == 0) {
		setBackNext(getP(), slot);
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
		    set();
		    return;
		}

		showSite();
	    }
	});

	setItemLeftAction(slot + 8, Items.createitem(Material.STAINED_GLASS_PANE, "Next", Arrays.asList(ChatColor.GRAY + "Go to the next side")), new AbstractAction() {

	    public void click(Player player) {
		site++;
		showSite();
	    }
	});
    }

    public void addAbility(int slot) {

	Ability abi = ability.get(abilities);

	if (abi != null) {

	    ArrayList<String> lore = new ArrayList<>();
	    lore.add(ChatColor.GRAY + "Left click to add this ability");
	    lore.add(ChatColor.GRAY + "to the tradeup!");
	    lore.add(ChatColor.WHITE + "Points with this kit " + (abilitypoints + abi.getKitpoints()) + "/50");

	    ItemStack item = abi.getInvItem().clone();
	    ItemMeta meta = item.getItemMeta();
	    meta.setLore(lore);
	    item.setItemMeta(meta);

	    setItemLeftAction(slot, item, new AbstractAction() {

		public void click(Player player) {

		    ability.remove(abilities, abi);
		    getTradeupabilities().add(abi);
		    setAbilitypoints(getAbilitypoints() + abi.getKitpoints());

		    if (getAbilitypoints() > 50) {

			for (Ability abi : getTradeupabilities())
			    new Loader().removeAbility(abi, getP());

			delete();
			new Chest(player);
		    } else
			set();
		}
	    });
	} else
	    setItem(slot, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);

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

    public int getAbilitypoints() {
	return abilitypoints;
    }

    public void setAbilitypoints(int points) {
	this.abilitypoints = points;
    }

    public ArrayList<Ability> getTradeupabilities() {
	return tradeupabilities;
    }

    public void setTradeupabilities(ArrayList<Ability> tradeupabilities) {
	this.tradeupabilities = tradeupabilities;
    }

}
