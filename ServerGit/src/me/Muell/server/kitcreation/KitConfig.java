package me.Muell.server.kitcreation;

import java.util.HashSet;

import me.Muell.server.abilities.Ability;
import me.Muell.server.playstyle.Playstyle;

public class KitConfig {

    private HashSet<Ability> abilities = new HashSet<>();
    private String name;
    private Playstyle style;
    private int slot;
    private int points;

    public KitConfig(int slot) {

	this.slot = slot;
    }

    public HashSet<Ability> getAbilities() {
	return abilities;
    }

    public void setAbilities(HashSet<Ability> abilities) {
	this.abilities = abilities;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Playstyle getStyle() {
	return style;
    }

    public void setStyle(Playstyle style) {
	this.style = style;
    }

    public int getSlot() {
	return slot;
    }

    public void setSlot(int slot) {
	this.slot = slot;
    }

    public int getPoints() {
	return points;
    }

    public void setPoints(int points) {
	this.points = points;
    }

}
