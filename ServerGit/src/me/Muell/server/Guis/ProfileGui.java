package me.Muell.server.Guis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.playstyle.Playstyle;
import me.Muell.server.types.Items;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class ProfileGui extends AbstractGui {

    private Player target;
    private Player p;
    private OfflinePlayer offlinetarget;
    private int chests;
    private int points;
    private int site = 1;
    private int abilities = 1;
    private HashMap<Integer, Ability> ability = new HashMap<>();

    public ProfileGui(Player p, Player target) {
	super(p, 54, target.getName(), true);

	this.target = target;
	this.p = p;

	set();
	p.openInventory(getInv());
    }

    public ProfileGui(Player p, OfflinePlayer target) {
	super(p, 18, target.getName() + " : offline", true);

	this.offlinetarget = target;
	this.p = p;

	this.setChests(Main.getPlugin().getConfig().getInt("Players." + p.getUniqueId().toString() + ".chests"));
	this.setPoints(Main.getPlugin().getConfig().getInt("Players." + p.getUniqueId().toString() + ".points"));

	setOffline();
	p.openInventory(getInv());
    }

    public void set() {

	clearGui();

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	PlayerData td = Main.getPlayerData(target);
	Loader loader = new Loader();

	ArrayList<String> pingstring = new ArrayList<>();
	int ping = loader.getPing(target);

	if (ping == 0)
	    pingstring.add("" + ChatColor.GRAY + "offline");
	else if (ping > 0)
	    pingstring.add("" + ChatColor.GREEN + ping);
	else if (ping > 25)
	    pingstring.add("" + ChatColor.DARK_GREEN + ping);
	else if (ping > 50)
	    pingstring.add("" + ChatColor.YELLOW + ping);
	else if (ping > 100)
	    pingstring.add("" + ChatColor.GOLD + ping);
	else if (ping > 200)
	    pingstring.add("" + ChatColor.RED + ping);
	else if (ping > 300)
	    pingstring.add("" + ChatColor.DARK_RED + ping);

	ArrayList<String> abis = new ArrayList<>();
	for (Ability abi : td.getKit())
	    abis.add(ChatColor.GRAY + abi.getName());

	setIngameItem(td);
	setItem(1, Items.createitem(Material.WORKBENCH, "Abilities:", abis), null, null);

	setItem(2, Items.createitem(Material.CHEST, "Owned abilities:", Arrays.asList(ChatColor.WHITE + "Leftclick to see the abilities", ChatColor.WHITE + "Right click to reset!")),
		new AbstractAction() {

		    public void click(Player player) {

			setAbilities();

		    }
		}, new AbstractAction() {

		    public void click(Player player) {

			loader.clearAbilities(target);
			p.sendMessage(ChatColor.GREEN + "Successfully cleared " + target.getName() + "'s abilities!");
		    }
		});

	setItem(3, Items.createitem(Material.STRING, "Ping:", pingstring), null, null);

	setChestItem(td);
	setPointsItem(td);

	setItemRightAction(8, Items.createDye(DyeColor.RED, ChatColor.RED + "Reset profile", Arrays.asList(ChatColor.WHITE + "Right click")), new AbstractAction() {

	    public void click(Player player) {

		loader.clearAbilities(target);
		td.setChests(0);
		td.setPoints(0);
		td.setPlaystyle(Playstyle.SOUP);
	    }
	});

    }

    public void setIngameItem(PlayerData td) {

	setItemLeftAction(0, Items.createitem(Material.STONE_SWORD, "Ingame: " + td.isIngame(), null), new AbstractAction() {

	    public void click(Player player) {

		if (td.isIngame())
		    td.setIngame(false);

		else if (!td.isIngame())
		    td.setIngame(true);

		setIngameItem(td);
	    }
	});

    }

    public void setChestItem(PlayerData td) {

	setItem(4, Items.createitem(Material.CHEST, "Chests: " + td.getChests(), null), new AbstractAction() {

	    public void click(Player player) {

		new Loader().changeChests(1, target, p, false);
		setChestItem(td);
	    }
	}, new AbstractAction() {

	    public void click(Player player) {

		new Loader().changeChests(-1, target, p, false);
		setChestItem(td);

	    }
	});
    }

    public void setPointsItem(PlayerData td) {

	setItem(5, Items.createitem(Material.EMERALD, "Points: " + td.getPoints(), null), new AbstractAction() {

	    public void click(Player player) {

		new Loader().changePoints(100, target, p, false);
		setPointsItem(td);
	    }
	}, new AbstractAction() {

	    public void click(Player player) {

		new Loader().changePoints(-100, target, p, false);
		setPointsItem(td);

	    }
	});
    }

    public void setOffline() {

	clearGui();

	for (int i = 0; i < getInv().getSize(); i++) {

	    setItem(i, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

	Loader loader = new Loader();

	List<String> abis = Main.getPlugin().getConfig().getStringList("Players." + p.getUniqueId().toString() + ".kitconfig");

	setItem(0, Items.createitem(Material.WORKBENCH, "Abilities:", abis), null, null);
	setItem(1, Items.createitem(Material.CHEST, "Owned abilities:", Arrays.asList(ChatColor.WHITE + "Leftclick to see the abilities", ChatColor.WHITE + "Right click to reset!")), null,
		new AbstractAction() {

		    public void click(Player player) {

			loader.clearAbilities(offlinetarget);
			p.sendMessage(ChatColor.GREEN + "Successfully cleared " + offlinetarget.getName() + "'s abilities!");
		    }
		});

	setItem(2, Items.createitem(Material.STRING, "Ping:", Arrays.asList(ChatColor.WHITE + "offline")), null, null);
	setItem(3, Items.createitem(Material.CHEST, "Chests: " + getChests(), null), new AbstractAction() {

	    public void click(Player player) {

		setChests(getChests() + 1);
		setOffline();
	    }
	}, new AbstractAction() {

	    public void click(Player player) {

		if (getChests() - 1 < 0) {
		    setChests(0);
		    return;
		}

		setChests(getChests() - 1);
		setOffline();

	    }
	});

	setItem(4, Items.createitem(Material.EMERALD, "Points: " + getPoints(), null), new AbstractAction() {

	    public void click(Player player) {

		setPoints(getPoints() + 100);
		setOffline();

	    }
	}, new AbstractAction() {

	    public void click(Player player) {

		if (getPoints() - 100 < 0) {
		    setPoints(0);
		    return;
		}

		setPoints(getPoints() - 100);
		setOffline();

	    }
	});

	setItemLeftAction(7, Items.createDye(DyeColor.GREEN, ChatColor.GREEN + "Save", Arrays.asList(ChatColor.WHITE + "Left click")), new AbstractAction() {

	    public void click(Player player) {

		Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".chests", getChests());
		Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".points", getPoints());
		Main.getPlugin().saveConfig();

		p.closeInventory();
		p.sendMessage(ChatColor.DARK_GREEN + "Successfully saved the profile!");

	    }
	});

	setItemRightAction(8, Items.createDye(DyeColor.RED, ChatColor.RED + "Reset profile", Arrays.asList(ChatColor.WHITE + "Right click")), new AbstractAction() {

	    public void click(Player player) {

		Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString(), null);
		Main.getPlugin().saveConfig();

		p.closeInventory();
		p.sendMessage(ChatColor.RED + "Reseted the profile!");

	    }
	});

    }

    public void setAbilities() {

	clearGui();

	PlayerData td = Main.getPlayerData(target);

	clearGui();
	ability.clear();

	int amount = 0;
	for (Ability abi : td.ownedAbilities) {

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
		    set();
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
	    lore.add(ChatColor.GRAY + "Right click to remove this ability");

	    ItemStack item = abi.getInvItem().clone();
	    ItemMeta meta = item.getItemMeta();
	    meta.setLore(lore);
	    item.setItemMeta(meta);

	    setItem(slot, item, null, new AbstractAction() {

		public void click(Player player) {

		    new Loader().removeAbility(abi, target);
		    setAbilities();
		}
	    });

	} else {

	    setItem(slot, Items.createGlassPane(DyeColor.GRAY, "*", null), null, null);
	}

    }

    public int getPoints() {
	return points;
    }

    public void setPoints(int points) {
	this.points = points;
    }

    public int getChests() {
	return chests;
    }

    public void setChests(int chests) {
	this.chests = chests;
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
