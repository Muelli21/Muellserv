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
import me.Muell.server.kitcreation.KitConfig;
import me.Muell.server.playstyle.Playstyle;
import me.Muell.server.types.Items;
import me.Muell.server.types.PlayerData;

public class KitConfigGui extends AbstractGui {

    private int site = 1;
    private int abilities = 1;

    public KitConfigGui(Player p, KitConfig config) {
	super(p, 27, "Kitcreation: " + config.getName(), true);

	PlayerData pd = Main.getPlayerData(p);
	HashMap<Integer, Ability> ability = new HashMap<>();

	int amount = 0;
	for (Ability abi : pd.ownedAbilities) {

	    if (!ability.containsValue(abi)) {
		amount++;
		ability.put(amount, abi);
	    }
	}

	set(p, config, ability);
	p.openInventory(getInv());
    }

    public KitConfigGui(Player p, KitConfig config, boolean sense) {
	super(p, 9, "Current abilities", true);

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	int slot = 0;

	for (Ability abi : config.getAbilities()) {

	    setItemRightAction(slot, abi.getInvItem(), new AbstractAction() {

		public void click(Player player) {

		    config.getAbilities().remove(abi);
		    config.setPoints(config.getPoints() - abi.getKitpoints());
		    new KitConfigGui(p, config);
		}
	    });

	    slot++;
	}

	setItemLeftAction(8, Items.createitem(Material.STAINED_GLASS_PANE, "Back", null), new AbstractAction() {

	    public void click(Player player) {

		new KitConfigGui(p, config);
	    }

	});
	p.openInventory(getInv());
    }

    public KitConfigGui(Player p, KitConfig config, HashMap<Integer, Ability> ability) {
	super(p, 54, "Add an ability", true);

	showSite(p, config, ability);
	p.openInventory(getInv());
    }

    public KitConfigGui(Player p, Ability abi, int slot, KitConfig config, HashMap<Integer, Ability> ability) {
	super(p, 9, "Details", true);

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	int combined = config.getPoints() + abi.getKitpoints();

	setItem(0, abi.getInvItem(), null, null);
	setItem(1, Items.createitem(Material.WOOD_BUTTON, "Abilitypoints: " + abi.getKitpoints(), null), null, null);
	setItem(2, Items.createitem(Material.WORKBENCH, "Kitpoints: " + combined, Arrays.asList(ChatColor.GRAY + "With this ability")), null, null);

	setItemLeftAction(3, Items.createDye(DyeColor.GREEN, "Add the ability to the kit", null), new AbstractAction() {

	    public void click(Player player) {

		if (config.getPoints() + abi.getKitpoints() > 25) {

		    p.sendMessage(ChatColor.RED + "You can not add this ability");
		    p.sendMessage(ChatColor.RED + "The maximum amount of kitpoints has already been reached!");
		    return;
		}

		config.getAbilities().add(abi);
		config.setPoints(config.getPoints() + abi.getKitpoints());
		new KitConfigGui(p, config, ability);
	    }
	});

	setItemLeftAction(8, Items.createitem(Material.STAINED_GLASS_PANE, "Back", null), new AbstractAction() {

	    public void click(Player player) {

		new KitConfigGui(p, config);
	    }
	});
	p.openInventory(getInv());
    }

    public void showSite(Player p, KitConfig config, HashMap<Integer, Ability> ability) {

	clearGui();
	for (int slot = 0; slot < 54; slot++) {

	    if (slot % 9 == 0) {
		setBackNext(p, slot, config, ability);
		continue;
	    }

	    if ((slot + 1) % 9 == 0)
		continue;

	    addAbility(p, slot, config, ability);
	    abilities++;
	}
    }

    public void addAbility(Player p, int slot, KitConfig config, HashMap<Integer, Ability> ability) {

	Ability abi = ability.get(abilities);

	if (abi != null) {

	    if (!config.getAbilities().contains(abi)) {

		setAbiItem(p, abi, slot, config, ability);

	    } else {

		setAddedItem(p, abi, slot, config, ability);
	    }
	} else {

	    setItem(slot, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}
    }

    public void setAddedItem(Player p, Ability abi, int slot, KitConfig config, HashMap<Integer, Ability> ability) {

	setItemRightAction(slot, Items.createGlassPane(DyeColor.RED, abi.getName(), Arrays.asList(ChatColor.WHITE + "Right click to remove!")), new AbstractAction() {

	    public void click(Player player) {

		config.getAbilities().remove(abi);
		config.setPoints(config.getPoints() - abi.getKitpoints());
		setAbiItem(p, abi, slot, config, ability);
	    }
	});
    }

    public void setAbiItem(Player p, Ability abi, int slot, KitConfig config, HashMap<Integer, Ability> ability) {

	int combined = config.getPoints() + abi.getKitpoints();
	ArrayList<String> lore = new ArrayList<>();
	lore.add(ChatColor.GRAY + "Left click to choose the ability");
	lore.add(ChatColor.GRAY + "Right click to see futher details");
	lore.add(ChatColor.GRAY + "Abilitypoints: " + abi.getKitpoints());
	lore.add(ChatColor.GRAY + "Kitpoints: " + combined);

	ItemStack item = abi.getInvItem().clone();
	ItemMeta meta = item.getItemMeta();
	meta.setLore(lore);
	item.setItemMeta(meta);

	setItem(slot, item, new AbstractAction() {

	    public void click(Player player) {

		if (config.getPoints() + abi.getKitpoints() > 25) {

		    p.sendMessage(ChatColor.RED + "You can not add this ability");
		    p.sendMessage(ChatColor.RED + "The maximum amount of kitpoints has already been reached!");
		    return;
		}

		config.getAbilities().add(abi);
		config.setPoints(config.getPoints() + abi.getKitpoints());
		setAddedItem(p, abi, slot, config, ability);

	    }
	}, new AbstractAction() {

	    public void click(Player player) {

		new KitConfigGui(getP(), abi, slot, config, ability);
	    }
	});
    }

    public void setBackNext(Player p, int slot, KitConfig config, HashMap<Integer, Ability> ability) {

	setItemLeftAction(slot, Items.createitem(Material.STAINED_GLASS_PANE, "Back", Arrays.asList(ChatColor.GRAY + "Go back")), new AbstractAction() {

	    public void click(Player player) {
		site--;
		setAbilities(getAbilities() - 84);

		if (site < 1) {
		    new KitConfigGui(p, config);
		}

		showSite(p, config, ability);
	    }
	});

	setItemLeftAction(slot + 8, Items.createitem(Material.STAINED_GLASS_PANE, "Next", Arrays.asList(ChatColor.GRAY + "Go to the next side")), new AbstractAction() {

	    public void click(Player player) {
		site++;
		showSite(p, config, ability);
	    }
	});
    }

    public void set(Player p, KitConfig config, HashMap<Integer, Ability> ability) {

	clearGui();

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	PlayerData pd = Main.getPlayerData(p);

	ArrayList<String> name = new ArrayList<>();
	for (Ability abicurrent : config.getAbilities())
	    name.add(ChatColor.GRAY + abicurrent.getName());

	setItemLeftAction(4, Items.createitem(Material.ANVIL, "Rename this config", Arrays.asList(ChatColor.GRAY + "Current name: " + config.getName())), null);
	setItemLeftAction(11, Items.createitem(Material.CHEST, "Add a ability", null), new AbstractAction() {

	    public void click(Player player) {

		new KitConfigGui(p, config, ability);
	    }
	});

	setItemLeftAction(13, Items.createitem(Material.WORKBENCH, "Kitpoints: ", Arrays.asList(ChatColor.GRAY + "" + config.getPoints())), null);
	setItemLeftAction(15, Items.createitem(Material.ENDER_CHEST, "Current abilities: ", name), new AbstractAction() {

	    public void click(Player player) {

		new KitConfigGui(p, config, true);
	    }
	});

	setItemLeftAction(18, Items.createDye(DyeColor.GREEN, "Save", null), new AbstractAction() {

	    public void click(Player player) {

		pd.getKitconfigs().remove(config.getSlot());
		pd.getKitconfigs().put(config.getSlot(), config);
		p.sendMessage(ChatColor.DARK_GREEN + "Successfully saved the config!");
		delete();
	    }
	});

	setStyleItem(config, ability);

	setItemRightAction(26, Items.createDye(DyeColor.RED, "Reset", Arrays.asList(ChatColor.GRAY + "Right click to reset")), new AbstractAction() {

	    public void click(Player player) {

		config.getAbilities().clear();
		config.setName("" + config.getSlot());
		config.setPoints(0);
		config.setStyle(Playstyle.SOUP);
		set(p, config, ability);
	    }
	});

    }

    public void setStyleItem(KitConfig config, HashMap<Integer, Ability> ability) {

	setItemLeftAction(22, config.getStyle().getInvItem(), new AbstractAction() {

	    public void click(Player player) {

		int i = config.getStyle().ordinal();
		Playstyle style = Playstyle.values()[i++ >= Playstyle.values().length - 1 ? i = 0 : i];

		config.setStyle(style);
		setStyleItem(config, ability);
	    }
	});

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
