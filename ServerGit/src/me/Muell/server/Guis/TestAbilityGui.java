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
import me.Muell.server.types.PlayerData;

public class TestAbilityGui extends AbstractGui {

    private Player p;
    private int site = 1;
    private int abilities = 1;
    private HashMap<Integer, Ability> ability = new HashMap<>();

    public TestAbilityGui(Player p) {
	super(p, 54, "Test a ability", true);

	this.p = p;
	setAbilities();
	p.openInventory(getInv());
    }

    public void setAbilities() {

	clearGui();

	clearGui();
	ability.clear();

	int amount = 0;
	for (Ability abi : Ability.values()) {

	    amount++;
	    ability.put(amount, abi);
	    abilities = 1;
	    site = 1;
	}

	showSite(p);
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
		    clearGui();
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
	    lore.add(ChatColor.GRAY + "Left click to add this ability");
	    lore.add(ChatColor.GRAY + "temporarely to your kit!");

	    ItemStack item = abi.getInvItem().clone();
	    ItemMeta meta = item.getItemMeta();
	    meta.setLore(lore);
	    item.setItemMeta(meta);

	    setItemLeftAction(slot, item, new AbstractAction() {

		public void click(Player player) {

		    PlayerData pd = Main.getPlayerData(p);
		    pd.getKit().add(abi);

		    for (int length = abi.getItems().length; length > 0;) {
			p.getInventory().clear(8 - length + 1);
			length--;
		    }

		    for (ItemStack items : abi.getItems()) {
			p.getInventory().addItem(items);
		    }

		    p.sendMessage("You are now testing the ability " + abi.getName());
		    delete();
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

}
