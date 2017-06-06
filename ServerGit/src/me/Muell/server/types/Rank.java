package me.Muell.server.types;

import java.util.List;

import me.Muell.server.Main;

public enum Rank {

    OWNER(
	    "Owner",
	    "§4"),
    ADMIN(
	    "Admin",
	    "§c"),
    MODERATOR(
	    "Moderator",
	    "§5"),
    TRIAL_MODERATOR(
	    "Trial-Moderator",
	    "§d"),
    HEAD_BUILDER(
	    "Head-Builder",
	    "§6"),
    BUILDER(
	    "Builder",
	    "§e"),
    PLAYER(
	    "Player",
	    "§f");

    private String prefix;
    private String name;
    private List<String> permissions;

    private Rank(String name, String prefix) {

	this.name = name;
	this.prefix = prefix;
	this.permissions = Main.getPlugin().getConfig().getStringList("Permissions." + name);
    }

    public String getPrefix() {
	return prefix;
    }

    public String getName() {
	return name;
    }

    public List<String> getPermissions() {
	return permissions;
    }

}