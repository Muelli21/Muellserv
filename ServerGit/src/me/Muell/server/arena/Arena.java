package me.Muell.server.arena;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Arena {

    public static ArrayList<Arena> list = new ArrayList<Arena>();

    private boolean free;
    private String name;
    private Player p1;
    private Player p2;
    private Location spawn1;
    private Location spawn2;
    private Entity bot;

    public void arena(String name) {
	this.name = name;
	list.add(this);
    }

    public Arena(String name, Location spawn1, Location spawn2) {

	this.name = name;
	this.spawn1 = spawn1;
	this.spawn2 = spawn2;
	list.add(this);
    }

    public String getName() {

	return name;
    }

    public void setName(String name) {
	this.name = name;

    }

    public boolean isFree() {
	return free;
    }

    public void setFree(boolean free) {

	this.free = free;
    }

    public Player getP1() {
	return p1;
    }

    public void setP1(Player p1) {
	this.p1 = p1;

    }

    public Player getP2() {
	return p2;
    }

    public void setP2(Player p2) {
	this.p2 = p2;

    }

    public Location getSpawn1() {
	return spawn1;
    }

    public void setSpawn1(Location spawn1) {
	this.spawn1 = spawn1;
    }

    public Location getSpawn2() {
	return spawn2;
    }

    public void setSpawn2(Location spawn2) {
	this.spawn2 = spawn2;
    }

    public Entity getBot() {
	return bot;
    }

    public void setBot(Entity bot) {
	this.bot = bot;

    }

}
