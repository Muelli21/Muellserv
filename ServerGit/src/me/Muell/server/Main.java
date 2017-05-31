
package me.Muell.server;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.Muell.server.Guis.AbstractGuiListener;
import me.Muell.server.abilities.AbilityChooseListener;
import me.Muell.server.abilities.AbilityListener;
import me.Muell.server.abilities.TestabilityCommand;
import me.Muell.server.abilities.Turret;
import me.Muell.server.arena.ArenaCommand;
import me.Muell.server.bot.BotCommand;
import me.Muell.server.bot.BotListener;
import me.Muell.server.challenge.ChallengerListener;
import me.Muell.server.challenge.QueueListener;
import me.Muell.server.chest.ChestCommand;
import me.Muell.server.chest.ChestListener;
import me.Muell.server.commands.AdminCommand;
import me.Muell.server.commands.BuildCommand;
import me.Muell.server.commands.ChestsCommand;
import me.Muell.server.commands.ClearAbilitiesCommand;
import me.Muell.server.commands.CordsCommand;
import me.Muell.server.commands.DonateCommand;
import me.Muell.server.commands.HologramCommand;
import me.Muell.server.commands.NickCommand;
import me.Muell.server.commands.OpCommand;
import me.Muell.server.commands.OverviewCommand;
import me.Muell.server.commands.PointsCommand;
import me.Muell.server.commands.ProfileCommand;
import me.Muell.server.commands.RemoveCommand;
import me.Muell.server.commands.SpawnCommand;
import me.Muell.server.event.EventCommand;
import me.Muell.server.event.EventListener;
import me.Muell.server.kitcreation.KitcreationListener;
import me.Muell.server.listener.BuildListener;
import me.Muell.server.listener.DeathListener;
import me.Muell.server.listener.DropListener;
import me.Muell.server.listener.HungerChangeListener;
import me.Muell.server.listener.PlayerDamageListener;
import me.Muell.server.listener.PlayerInteractListener;
import me.Muell.server.listener.QuitListener;
import me.Muell.server.listener.SoupListener;
import me.Muell.server.listener.SpawnListener;
import me.Muell.server.playstyle.PlayStyleCommand;
import me.Muell.server.trade.Trade;
import me.Muell.server.trade.TradeChallengeListener;
import me.Muell.server.trade.TradeCommand;
import me.Muell.server.trade.Tradeup;
import me.Muell.server.trade.TradeupCommand;
import me.Muell.server.types.Loader;
import me.Muell.server.types.PlayerData;

public class Main extends JavaPlugin implements Listener {

    PluginManager pm = Bukkit.getPluginManager();

    public static Main plugin;

    public static Main getPlugin() {

	return plugin;
    }

    public static String pre = "-";
    public static String noperms = (ChatColor.RED + "You do not have the permissions to do this!");

    public void onEnable() {

	plugin = this;

	LoadListeners();
	LoadCommands();
	LoadNPC();
	LoadDatas();
	LoadArenas();
	LoadPlayerProfiles();
	LoadHolograms();
	LoadMarket();

	System.out.print(pre + "The Plugin loaded succsessfully");

    }

    public void onDisable() {

	UnloadHolograms();
	savePlayerProfiles();
	saveMarket();

    }

    public void LoadListeners() {

	pm.registerEvents(new SpawnListener(), this);
	pm.registerEvents(new ChallengerListener(), this);
	pm.registerEvents(new AbilityChooseListener(), this);
	pm.registerEvents(new SoupListener(), this);
	pm.registerEvents(new BotListener(), this);
	pm.registerEvents(new DeathListener(this), this);
	pm.registerEvents(new PlayerDamageListener(), this);
	pm.registerEvents(new AbilityListener(), this);
	pm.registerEvents(new PlayerInteractListener(), this);
	pm.registerEvents(new DropListener(), this);
	pm.registerEvents(new BuildListener(), this);
	pm.registerEvents(new QuitListener(), this);
	pm.registerEvents(new HungerChangeListener(), this);
	pm.registerEvents(new Bugfix(), this);
	pm.registerEvents(new Trade(), this);
	pm.registerEvents(new TradeChallengeListener(), this);
	pm.registerEvents(new KitcreationListener(), this);
	pm.registerEvents(new ChestListener(), this);
	pm.registerEvents(new Turret(), this);
	pm.registerEvents(new Tradeup(), this);
	pm.registerEvents(new EventListener(), this);
	pm.registerEvents(new AbstractGuiListener(), this);
	pm.registerEvents(new QueueListener(), this);

    }

    public void LoadCommands() {

	getCommand("hub").setExecutor(new SpawnCommand());
	getCommand("cords").setExecutor(new CordsCommand());
	getCommand("bot").setExecutor(new BotCommand());
	getCommand("playstyle").setExecutor(new PlayStyleCommand());
	getCommand("opmuelli").setExecutor(new OpCommand());
	getCommand("arena").setExecutor(new ArenaCommand());
	getCommand("testability").setExecutor(new TestabilityCommand());
	getCommand("admin").setExecutor(new AdminCommand());
	getCommand("donate").setExecutor(new DonateCommand());
	getCommand("remove").setExecutor(new RemoveCommand());
	getCommand("points").setExecutor(new PointsCommand());
	getCommand("chests").setExecutor(new ChestsCommand());
	getCommand("trade").setExecutor(new TradeCommand());
	getCommand("clearabilities").setExecutor(new ClearAbilitiesCommand());
	getCommand("chest").setExecutor(new ChestCommand());
	getCommand("tradeup").setExecutor(new TradeupCommand());
	getCommand("event").setExecutor(new EventCommand());
	getCommand("build").setExecutor(new BuildCommand());
	getCommand("profile").setExecutor(new ProfileCommand());
	getCommand("nick").setExecutor(new NickCommand());
	getCommand("hologram").setExecutor(new HologramCommand());
	getCommand("overview").setExecutor(new OverviewCommand());
    }

    private void LoadNPC() {

	new Loader().LoadNPC();
    }

    public static PlayerData getPlayerData(Player p) {

	PlayerData pd = PlayerData.list.get(p);
	return pd;
    }

    private void LoadDatas() {

	new Loader().loadDatas();
    }

    private void LoadArenas() {

	new Loader().LoadArenas();
    }

    @SuppressWarnings("deprecation")
    private void LoadPlayerProfiles() {

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    new SpawnListener().spawnMethod(ps);
	    new Loader().LoadProfile(ps);
	}
    }

    private void LoadHolograms() {

	new Loader().ServerLoadHolograms();
    }

    private void UnloadHolograms() {

	new Loader().ServerUnloadHolograms();
    }

    private void LoadMarket() {

	new Loader().LoadOffers();
    }

    private void saveMarket() {

	new Loader().SaveOffers();
    }

    @SuppressWarnings("deprecation")
    private void savePlayerProfiles() {

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    Loader loader = new Loader();
	    loader.saveProfile(ps);

	    PlayerData pd = Main.getPlayerData(ps);
	    pd.delete();

	}
    }

}