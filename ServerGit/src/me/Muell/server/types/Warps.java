package me.Muell.server.types;

import org.bukkit.Location;

import me.Muell.server.Main;

public enum Warps {

    SPAWN(
	    new Location(Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.hub.world")), Main.getPlugin().getConfig().getDouble("cords.hub.x"),
		    Main.getPlugin().getConfig().getDouble("cords.hub.y"), Main.getPlugin().getConfig().getDouble("cords.hub.z"), (float) Main.getPlugin().getConfig().getDouble("cords.hub.yaw"),
		    (float) Main.getPlugin().getConfig().getDouble("cords.hub.pitch"))),
    ONEVSONE(
	    new Location(Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.1vs1.world")), Main.getPlugin().getConfig().getDouble("cords.1vs1.x"),
		    Main.getPlugin().getConfig().getDouble("cords.1vs1.y"), Main.getPlugin().getConfig().getDouble("cords.1vs1.z"), (float) Main.getPlugin().getConfig().getDouble("cords.1vs1.yaw"),
		    (float) Main.getPlugin().getConfig().getDouble("cords.1vs1.pitch"))),
    FFA(
	    new Location(Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.ffa.world")), Main.getPlugin().getConfig().getDouble("cords.ffa.x"),
		    Main.getPlugin().getConfig().getDouble("cords.ffa.y"), Main.getPlugin().getConfig().getDouble("cords.ffa.z"), (float) Main.getPlugin().getConfig().getDouble("cords.ffa.yaw"),
		    (float) Main.getPlugin().getConfig().getDouble("cords.ffa.pitch"))),

    FPS(
	    new Location(Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.fps.world")), Main.getPlugin().getConfig().getDouble("cords.fps.x"),
		    Main.getPlugin().getConfig().getDouble("cords.fps.y"), Main.getPlugin().getConfig().getDouble("cords.fps.z"), (float) Main.getPlugin().getConfig().getDouble("cords.fps.yaw"),
		    (float) Main.getPlugin().getConfig().getDouble("cords.fps.pitch"))),
    
    GAMES(
	    new Location(Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.games.world")), Main.getPlugin().getConfig().getDouble("cords.games.x"),
		    Main.getPlugin().getConfig().getDouble("cords.games.y"), Main.getPlugin().getConfig().getDouble("cords.games.z"), (float) Main.getPlugin().getConfig().getDouble("cords.games.yaw"),
		    (float) Main.getPlugin().getConfig().getDouble("cords.games.pitch"))),

    CLASSIC(
	    new Location(Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.classic.world")), Main.getPlugin().getConfig().getDouble("cords.classic.x"),
		    Main.getPlugin().getConfig().getDouble("cords.classic.y"), Main.getPlugin().getConfig().getDouble("cords.classic.z"),
		    (float) Main.getPlugin().getConfig().getDouble("cords.classic.yaw"), (float) Main.getPlugin().getConfig().getDouble("cords.classic.pitch"))),
    OLDSCHOOL(
	    new Location(Main.getPlugin().getServer().getWorld(Main.getPlugin().getConfig().getString("cords.oldschool.world")), Main.getPlugin().getConfig().getDouble("cords.oldschool.x"),
		    Main.getPlugin().getConfig().getDouble("cords.oldschool.y"), Main.getPlugin().getConfig().getDouble("cords.oldschool.z"),
		    (float) Main.getPlugin().getConfig().getDouble("cords.oldschool.yaw"), (float) Main.getPlugin().getConfig().getDouble("cords.oldschool.pitch")));

    private Location loc;

    private Warps(Location loc) {

	this.loc = loc;

    }

    public Location getLocation() {

	return this.loc;
    }

}
