package me.Muell.server.types;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Muell.server.Main;
import me.Muell.server.abilities.Ability;
import me.Muell.server.abilities.AbilityListener;
import me.Muell.server.arena.Arena;
import me.Muell.server.bot.CustomZombie;
import me.Muell.server.challenge.Queue;
import me.Muell.server.kitcreation.KitConfig;
import me.Muell.server.playstyle.PlayStyleInv;
import me.Muell.server.playstyle.Playstyle;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.EntityTypes;

public class Loader {

    // Load PlayerDatas
    @SuppressWarnings("deprecation")
    public void loadDatas() {

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    new PlayerData(ps);
	}

    }

    // Load Arenas from the config
    public void LoadArenas() {

	if (Main.getPlugin().getConfig().getConfigurationSection("Arena.name") == null) {
	    System.out.print("Keine Arenen gefunden!");

	    return;
	}

	for (String s : Main.getPlugin().getConfig().getConfigurationSection("Arena.name").getKeys(false)) {

	    String arenaname = s;

	    Location spawn1 = new Location(Bukkit.getWorld("world"), Main.getPlugin().getConfig().getDouble("Arena.name." + s + ".spawn1.x"),
		    Main.getPlugin().getConfig().getDouble("Arena.name." + s + ".spawn1.y"), Main.getPlugin().getConfig().getDouble("Arena.name." + s + ".spawn1.z"),
		    (float) Main.getPlugin().getConfig().getDouble("Arena.name." + s + ".spawn1.yaw"), (float) Main.getPlugin().getConfig().getDouble("Arena.name" + s + ".spawn1.pitch"));

	    Location spawn2 = new Location(Bukkit.getWorld("world"), Main.getPlugin().getConfig().getDouble("Arena.name." + s + ".spawn2.x"),
		    Main.getPlugin().getConfig().getDouble("Arena.name." + s + ".spawn2.y"), Main.getPlugin().getConfig().getDouble("Arena.name." + s + ".spawn2.z"),
		    (float) Main.getPlugin().getConfig().getDouble("Arena.name." + s + ".spawn2.yaw"), (float) Main.getPlugin().getConfig().getDouble("Arena.name." + s + ".spawn2.pitch"));

	    Arena arena = new Arena(arenaname, spawn1, spawn2);
	    arena.setFree(true);
	    arena.setSpawn1(spawn1);
	    arena.setSpawn2(spawn2);

	    System.out.print(ChatColor.GREEN + "Arena:" + arenaname);
	    System.out.print(ChatColor.GREEN + "Spawn1:" + spawn1);
	    System.out.print(ChatColor.GREEN + "Spawn2:" + spawn2);
	}

    }

    // Load NPCs, which means PvpBots
    @SuppressWarnings("unchecked")
    public void LoadNPC() {
	try {
	    Field c = EntityTypes.class.getDeclaredField("c");
	    Field f = EntityTypes.class.getDeclaredField("f");
	    c.setAccessible(true);
	    f.setAccessible(true);
	    ((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(CustomZombie.class, "Zombie");
	    ((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(CustomZombie.class, 54);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void LoadProfile(Player p) {

	PlayerData pd = Main.getPlayerData(p);

	if (Main.getPlugin().getConfig().get("Players." + p.getUniqueId().toString()) == null) {

	    for (int slot = 0; slot < 5; slot++) {

		Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".kitconfig." + slot + ".points", 0);
		Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".kitconfig." + slot + ".name", slot);
		Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".kitconfig." + slot + ".style", "Souper");
		Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".kitconfig." + slot + ".abilities", null);
		Main.getPlugin().saveConfig();
	    }

	    Main.getPlugin().getConfig().get("Players." + p.getUniqueId().toString());
	    Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".lastname", p.getName());
	    Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".chests", 4);
	    Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".abilities", null);
	    Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".points", 333);
	    Main.getPlugin().saveConfig();

	    p.sendMessage(ChatColor.GREEN + "Successfully created your profile!");
	    return;
	}

	Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".lastname", p.getName());
	Main.getPlugin().saveConfig();

	List<String> abilities = Main.getPlugin().getConfig().getStringList("Players." + p.getUniqueId().toString() + ".abilities");
	String rank = Main.getPlugin().getConfig().getString("Players." + p.getUniqueId().toString() + ".rank");
	int chests = Main.getPlugin().getConfig().getInt("Players." + p.getUniqueId().toString() + ".chests");
	int points = Main.getPlugin().getConfig().getInt("Players." + p.getUniqueId().toString() + ".points");

	pd.setChests(chests);
	pd.setPoints(points);
	pd.setRank(getRank(rank));

	for (String name : abilities)
	    pd.ownedAbilities.add(getAbility(name));

	if (Main.getPlugin().getConfig().getConfigurationSection("Players." + p.getUniqueId().toString() + ".kitconfig") != null) {

	    for (String slot : Main.getPlugin().getConfig().getConfigurationSection("Players." + p.getUniqueId().toString() + ".kitconfig").getKeys(false)) {

		int slotint = Integer.valueOf(slot);
		int kitpoints = Main.getPlugin().getConfig().getInt("Players." + p.getUniqueId().toString() + ".kitconfig." + slot + ".points");
		String name = Main.getPlugin().getConfig().getString("Players." + p.getUniqueId().toString() + ".kitconfig." + slot + ".name");
		String style = Main.getPlugin().getConfig().getString("Players." + p.getUniqueId().toString() + ".kitconfig." + slot + ".style");
		List<String> abinames = Main.getPlugin().getConfig().getStringList("Players." + p.getUniqueId().toString() + ".kitconfig." + slot + ".abilities");

		HashSet<Ability> kitabis = new HashSet<>();

		for (String names : abinames)
		    kitabis.add(getAbility(names));

		KitConfig config = new KitConfig(slotint);
		config.setName(name);
		config.setPoints(kitpoints);
		config.setAbilities(kitabis);
		config.setStyle(getPlaystyle(style));
		pd.getKitconfigs().put(slotint, config);
	    }
	}

	p.sendMessage(ChatColor.GREEN + "Successfully loaded your profile!");
    }

    public void saveProfile(Player p) {

	PlayerData pd = Main.getPlayerData(p);

	List<String> abilities = new ArrayList<String>();

	for (Ability abi : pd.ownedAbilities)
	    abilities.add(abi.getName());

	for (KitConfig config : pd.getKitconfigs().values()) {

	    List<String> abis = new ArrayList<String>();

	    for (Ability abi : config.getAbilities())
		abis.add(abi.getName());

	    String name = config.getName();
	    String style = config.getStyle().getName();
	    int points = config.getPoints();
	    Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".kitconfig." + config.getSlot() + ".name", name);
	    Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".kitconfig." + config.getSlot() + ".style", style);
	    Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".kitconfig." + config.getSlot() + ".abilities", abis);
	    Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".kitconfig." + config.getSlot() + ".points", points);
	    Main.getPlugin().saveConfig();
	}

	Main.getPlugin().getConfig().get("Players." + p.getUniqueId().toString());
	Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".lastname", p.getName());
	Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".chests", pd.getChests());
	Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".points", pd.getPoints());
	Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".abilities", abilities);
	Main.getPlugin().getConfig().set("Players." + p.getUniqueId().toString() + ".rank", pd.getRank().getName());
	Main.getPlugin().saveConfig();

	p.sendMessage(ChatColor.GREEN + "Successfully saved your profile!");
    }

    public Ability getAbility(String name) {

	for (Ability allabilities : Ability.values()) {

	    if (allabilities.getName().equals(name))
		return allabilities;
	}

	return null;
    }

    public Playstyle getPlaystyle(String name) {

	for (Playstyle style : Playstyle.values()) {

	    if (style.getName().equals(name))
		return style;
	}

	return null;
    }

    public Ability getrandomAbility() {

	int abilityamount = Ability.values().length - 1;
	int randomability = (int) (Math.random() * abilityamount + 1);

	for (Ability abis : Ability.values()) {

	    if (randomability == 1) { return abis; }

	    randomability--;
	}

	return null;
    }

    public void removeAbility(Ability abi, OfflinePlayer target) {

	if (target.isOnline()) {

	    Player onlinetarget = (Player) target;

	    PlayerData td = Main.getPlayerData(onlinetarget);

	    if (abi == null)
		return;

	    td.ownedAbilities.remove(abi);

	    for (KitConfig config : td.getKitconfigs().values()) {

		if (config.getAbilities().contains(abi))

		    if (!td.ownedAbilities.contains(abi)) {
			config.getAbilities().remove(abi);
			config.setPoints(config.getPoints() - abi.getKitpoints());
		    }
	    }

	} else

	if (!target.isOnline()) {

	    String uuid = target.getUniqueId().toString();

	    List<String> ownedabilities = Main.getPlugin().getConfig().getStringList("Players." + uuid + ".abilities");

	    if (ownedabilities.contains(abi.getName()))
		ownedabilities.remove(abi.getName());

	    for (String slot : Main.getPlugin().getConfig().getConfigurationSection("Players." + uuid + ".kitconfig").getKeys(false)) {

		List<String> abinames = Main.getPlugin().getConfig().getStringList("Players." + uuid + ".kitconfig." + slot + ".abilities");

		if (abinames.contains(abi.getName()))
		    abinames.remove(abi.getName());

		Main.getPlugin().getConfig().set("Players." + uuid + ".kitconfig." + slot + ".abilities", abinames);
	    }

	    Main.getPlugin().getConfig().set("Players." + uuid + ".abilities", ownedabilities);
	    Main.getPlugin().saveConfig();
	}
    }

    public void clearAbilities(OfflinePlayer target) {

	if (target.isOnline()) {

	    Player onlinetarget = (Player) target;
	    PlayerData td = Main.getPlayerData(onlinetarget);
	    td.ownedAbilities.clear();

	    for (int slot = 0; slot < td.getKitconfigs().size(); slot++) {

		KitConfig config = td.getKitconfigs().get(slot);
		config.getAbilities().clear();
		config.setName("" + slot);
		config.setPoints(0);
		config.setStyle(Playstyle.SOUP);
	    }

	    onlinetarget.sendMessage(ChatColor.GREEN + "Your abilities have been cleared!");
	}

	if (!target.isOnline()) {

	    String uuid = target.getUniqueId().toString();

	    List<String> ownedabilities = Main.getPlugin().getConfig().getStringList("Players." + uuid + ".abilities");
	    ownedabilities.clear();

	    Main.getPlugin().getConfig().set("Players." + uuid + ".abilities", ownedabilities);

	    for (String slot : Main.getPlugin().getConfig().getConfigurationSection("Players." + uuid + ".kitconfig").getKeys(false)) {

		Main.getPlugin().getConfig().set("Players." + uuid + ".kitconfig." + slot + ".points", 0);
		Main.getPlugin().getConfig().set("Players." + uuid + ".kitconfig." + slot + ".name", slot);
		Main.getPlugin().getConfig().set("Players." + uuid + ".kitconfig." + slot + ".style", "Souper");
		Main.getPlugin().getConfig().set("Players." + uuid + ".kitconfig." + slot + ".abilities", null);
	    }

	    Main.getPlugin().saveConfig();
	}

    }

    public void ServerLoadHolograms() {

	if (Main.getPlugin().getConfig().get("Hologram.") == null) { return; }

	for (String s : Main.getPlugin().getConfig().getConfigurationSection("Hologram.").getKeys(false)) {

	    String name = s;
	    World world = Bukkit.getWorld(Main.getPlugin().getConfig().getString("Hologram." + s + ".world"));
	    Double x = Main.getPlugin().getConfig().getDouble("Hologram." + s + ".x");
	    Double y = Main.getPlugin().getConfig().getDouble("Hologram." + s + ".y");
	    Double z = Main.getPlugin().getConfig().getDouble("Hologram." + s + ".z");
	    List<String> text = Main.getPlugin().getConfig().getStringList("Hologram." + s + ".text");

	    Location loc = new Location(world, x, y, z);

	    Hologram hologram = new Hologram(loc, name);
	    hologram.set((ArrayList<String>) text);
	    hologram.show();

	    System.out.print(ChatColor.GREEN + name);
	}
    }

    public void LoadHolograms(Player p) {

	for (Hologram holograms : Hologram.allHolograms) {

	    holograms.show(p);
	}
    }

    public void ServerUnloadHolograms() {

	for (Hologram holograms : Hologram.allHolograms) {

	    holograms.destroy();
	}
    }

    public void SaveOffers() {

	List<String> offers = new ArrayList<>();

	for (Offer offer : Offer.offers)
	    offers.add(offer.getP().getName() + ":" + offer.getAbility().getName() + ":" + offer.getPrice() + ":" + offer.getDate());

	Main.getPlugin().getConfig().get("Offers");
	Main.getPlugin().getConfig().set("Offers", offers);
	Main.getPlugin().saveConfig();
    }

    @SuppressWarnings("deprecation")
    public void LoadOffers() {

	if (Main.getPlugin().getConfig().get("Offers") == null) { return; }

	List<String> offers = Main.getPlugin().getConfig().getStringList("Offers");

	for (String s : offers) {

	    String[] parts = s.split(":");
	    String playername = parts[0];
	    String abilityname = parts[1];
	    String pricestring = parts[2];
	    String dateString = parts[3];

	    OfflinePlayer p = Bukkit.getOfflinePlayer(playername);
	    Ability ability = getAbility(abilityname);
	    int price = Integer.valueOf(pricestring);
	    long date = Long.valueOf(dateString);

	    Bukkit.broadcastMessage(s);

	    new Offer(p, ability, price, date);
	}
    }

    public int getPing(OfflinePlayer p) {

	if (!p.isOnline())
	    return 0;

	CraftPlayer craftp = (CraftPlayer) p;
	int ping = craftp.getHandle().ping;

	return ping;

    }

    public void clearPlayer(Player p) {

	PlayerData pd = Main.getPlayerData(p);
	pd.setIngame(false);
	pd.getKit().clear();
	pd.setGamemode(Gamemode.SPAWN);
	pd.setAllowedtoseeplayers(true);
	pd.setAdminmode(false);
	pd.setDisguised(false);
	AbilityListener.playerswhousedJesus.remove(p);

	DisguiseManager disguise = new DisguiseManager();
	disguise.hideall(p);
	disguise.showCustom(p);
	disguise.showtoAll(p);
	Queue.playersinqueue.remove(p);

    }

    public void setKit(Player p, KitConfig config) {

	PlayerData pd = Main.getPlayerData(p);

	pd.setPlaystyle(config.getStyle());

	PlayStyleInv pinv = new PlayStyleInv();
	pinv.playStyleInv(p);

	ArrayList<ItemStack> items = new ArrayList<>();

	for (Ability abis : config.getAbilities()) {

	    pd.getKit().add(abis);
	    for (ItemStack item : abis.getItems())
		items.add(item);
	}

	for (int length = items.size(); length > 0;) {
	    p.getInventory().clear(8 - length + 1);
	    length--;
	}

	for (ItemStack item : items) {
	    p.getInventory().addItem(item);
	}

    }

    public void forceKit(Player p) {

	PlayerData pd = Main.getPlayerData(p);

	KitConfig config = pd.getKitconfigs().get(0);
	new Loader().setKit(p, config);
    }

    @SuppressWarnings("deprecation")
    public void addAbility(Ability ability, String name, CommandSender sender, boolean message) {

	if (Bukkit.getOfflinePlayer(name).isOnline()) {

	    Player target = Bukkit.getPlayer(name);
	    PlayerData td = Main.getPlayerData(target);

	    td.addAbility(ability);

	    if (message) {
		target.sendMessage(ChatColor.GREEN + sender.getName() + " donated you the ability " + ability.getName() + "!");
		sender.sendMessage(ChatColor.DARK_GREEN + "Successfully donated " + target.getName() + " the ability " + ability.getName() + "!");
	    }

	}

	if (!Bukkit.getOfflinePlayer(name).isOnline()) {

	    OfflinePlayer target = Bukkit.getOfflinePlayer(name);
	    String uuid = target.getUniqueId().toString();

	    List<String> ownedabilities = Main.getPlugin().getConfig().getStringList("Players." + uuid + ".abilities");
	    ownedabilities.add(ability.getName());

	    if (message)
		sender.sendMessage(ChatColor.DARK_GREEN + "Successfully donated " + target.getName() + " the ability " + ability.getName() + "!");

	    Main.getPlugin().getConfig().set("Players." + uuid + ".abilities", ownedabilities);
	    Main.getPlugin().saveConfig();

	}
    }

    public void changePoints(int points, OfflinePlayer target, CommandSender sender, boolean message) {

	if (points > 0) {

	    if (target.isOnline()) {

		Player onlinetarget = (Player) target;
		PlayerData td = Main.getPlayerData(onlinetarget);

		td.setPoints(td.getPoints() + points);

		if (message) {
		    onlinetarget.sendMessage(ChatColor.GREEN + sender.getName() + " donated you " + points + " point/s!");
		    sender.sendMessage(ChatColor.DARK_GREEN + "Successfully donated " + target.getName() + " " + points + " point/s!");
		}

	    }

	    if (!target.isOnline()) {

		String uuid = target.getUniqueId().toString();

		int currentpoints = Main.getPlugin().getConfig().getInt("Players." + uuid + ".points");

		Main.getPlugin().getConfig().set("Players." + uuid + ".points", currentpoints + points);
		Main.getPlugin().saveConfig();

		if (message) {
		    sender.sendMessage(ChatColor.DARK_GREEN + "Successfully donated " + target.getName() + " " + points + " point/s!");
		}
	    }
	}

	if (points < 0) {

	    if (target.isOnline()) {

		Player onlinetarget = (Player) target;
		PlayerData td = Main.getPlayerData(onlinetarget);

		if (td.getPoints() + points < 0)
		    td.setPoints(0);

		else if (td.getPoints() + points > 0)
		    td.setPoints(td.getPoints() + points);

		if (message) {
		    onlinetarget.sendMessage(ChatColor.GREEN + sender.getName() + " removed " + Math.abs(points) + " points from your account!");
		    sender.sendMessage(ChatColor.DARK_GREEN + "Successfully removed " + Math.abs(points) + " point/s from the the profile " + target.getName());
		}
	    }

	    if (!target.isOnline()) {

		String uuid = target.getUniqueId().toString();

		int currentpoints = Main.getPlugin().getConfig().getInt("Players." + uuid + ".points");

		Main.getPlugin().getConfig().set("Players." + uuid + ".points", currentpoints + points);
		Main.getPlugin().saveConfig();

		if (message) {
		    sender.sendMessage(ChatColor.DARK_GREEN + "Successfully removed " + Math.abs(points) + " point/s from the the profile " + target.getName());
		}
	    }

	}

    }

    public void changeChests(int chests, OfflinePlayer target, CommandSender sender, boolean message) {

	if (chests > 0) {

	    if (target.isOnline()) {

		Player onlinetarget = (Player) target;
		PlayerData td = Main.getPlayerData(onlinetarget);

		td.setChests(td.getChests() + chests);

		if (message) {
		    onlinetarget.sendMessage(ChatColor.GREEN + sender.getName() + " donated you " + chests + " chest/s!");
		    sender.sendMessage(ChatColor.DARK_GREEN + "Successfully donated " + target.getName() + " " + chests + " chest/s!");
		}

	    }

	    if (!target.isOnline()) {

		String uuid = target.getUniqueId().toString();

		int currentchests = Main.getPlugin().getConfig().getInt("Players." + uuid + ".chests");

		Main.getPlugin().getConfig().set("Players." + uuid + ".chests", currentchests + chests);
		Main.getPlugin().saveConfig();

		if (message) {
		    sender.sendMessage(ChatColor.DARK_GREEN + "Successfully donated " + target.getName() + " " + chests + " chest/s!");
		}
	    }
	}

	if (chests < 0) {

	    if (target.isOnline()) {

		Player onlinetarget = (Player) target;
		PlayerData td = Main.getPlayerData(onlinetarget);

		if (td.getChests() + chests < 0)
		    td.setChests(0);

		else if (td.getChests() + chests > 0)
		    td.setChests(td.getChests() + chests);

		if (message) {
		    onlinetarget.sendMessage(ChatColor.GREEN + sender.getName() + " removed " + Math.abs(chests) + " chests from your account!");
		    sender.sendMessage(ChatColor.DARK_GREEN + "Successfully removed " + Math.abs(chests) + " chest/s from the the profile " + target.getName());
		}
	    }

	    if (!target.isOnline()) {

		String uuid = target.getUniqueId().toString();

		int currentchests = Main.getPlugin().getConfig().getInt("Players." + uuid + ".chests");

		Main.getPlugin().getConfig().set("Players." + uuid + ".chests", currentchests + chests);
		Main.getPlugin().saveConfig();

		if (message) {
		    sender.sendMessage(ChatColor.DARK_GREEN + "Successfully removed " + Math.abs(chests) + " chest/s from the the profile " + target.getName());
		}
	    }

	}

    }

    public void setPrefix(Player p) {

	PlayerData pd = Main.getPlayerData(p);

	if (!pd.isNicked()) {
	    String name = pd.getRank().getPrefix() + p.getName() + ChatColor.RESET;
	    p.setDisplayName(name);
	    p.setPlayerListName(pd.getRank().getPrefix() + p.getName());

	}
    }

    public void setRank(OfflinePlayer target, Rank rank) {

	if (target.isOnline()) {
	    Player onlinetarget = (Player) target;
	    PlayerData td = Main.getPlayerData(onlinetarget);
	    td.setRank(rank);
	    setPrefix(onlinetarget);

	} else {

	    Main.getPlugin().getConfig().set("Players." + target.getUniqueId().toString() + ".rank", rank.getName());
	    Main.getPlugin().saveConfig();
	}
    }

    public Rank getRank(String name) {

	for (Rank rank : Rank.values()) {
	    if (rank.getName().equals(name))
		return rank;

	}

	return Rank.PLAYER;
    }
}
