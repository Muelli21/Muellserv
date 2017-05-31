package me.Muell.server.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.Muell.server.abilities.Ability;
import me.Muell.server.arena.Arena;
import me.Muell.server.kitcreation.KitConfig;
import me.Muell.server.playstyle.Playstyle;

public class PlayerData {

    public static HashMap<Player, PlayerData> list = new HashMap<>();

    private Player player, lastDamager = this.getPlayer(), tradeinvite;
    private boolean adminmode = false, frozen = false, ingame = false, disguised = false, challenge = false, build = false, allowedtoseeplayers = true;

    private Gamemode gamemode = Gamemode.SPAWN;

    private Arena currentArena;
    private Location frozenloc;
    private long lastReport;

    private double botspeed = 1;
    private double bothealth = 400;

    private int chests;
    private int points;

    private String nick, lastmessage;
    private List<String> duels = new ArrayList<String>();
    private HashMap<Ability, Long> abilitycooldown = new HashMap<>();

    private HashMap<Integer, KitConfig> kitconfigs = new HashMap<>();
    private ArrayList<Ability> kit = new ArrayList<>();

    private Playstyle style = Playstyle.SOUP;

    public ArrayList<Ability> ownedAbilities = new ArrayList<Ability>();

    public PlayerData(Player p) {

	this.player = p;
	list.put(p, this);

    }

    public void delete() {

	list.remove(this);
    }

    public List<String> getDuels() {

	return this.duels;

    }

    public boolean isInChallenge() {

	return this.challenge;

    }

    public void setInChallenge(boolean mode) {

	this.challenge = mode;

    }

    public String getLastMessage() {

	return this.lastmessage;

    }

    public void setLastMessage(String s) {

	this.lastmessage = s;

    }

    public String getNick() {

	return this.nick;

    }

    public void setNick(String nick) {

	this.nick = nick;

    }

    public void duel(Player p) {

	duels.add(p.getName());

    }

    public void removeDuel(String s) {

	duels.remove(s);

    }

    public void report() {

	this.lastReport = System.currentTimeMillis();
    }

    public long getLastReport() {

	return this.lastReport;

    }

    public Arena getCurrentArena() {

	return this.currentArena;

    }

    public void setCurrentArena(Arena arena) {

	this.currentArena = arena;

    }

    public Player getLastDamager() {

	return this.lastDamager;

    }

    public void setLastDamager(Player p) {

	this.lastDamager = p;

    }

    public void setDisguised(Boolean mode) {

	this.disguised = mode;

    }

    public Boolean isDisguised() {

	return this.disguised;

    }

    public void setIngame(Boolean mode) {

	this.ingame = mode;

    }

    public Boolean isIngame() {

	return this.ingame;
    }

    public void setAdminmode(boolean mode) {

	this.adminmode = mode;

    }

    public Boolean isInAdminmode() {

	return this.adminmode;

    }

    public void setFrozen(boolean mode) {

	this.frozen = mode;

    }

    public Boolean isFrozen() {

	return this.frozen;

    }

    public Player getPlayer() {

	return this.player;

    }

    public void setFrozenLocation(Location loc) {

	this.frozenloc = loc;

    }

    public Location getFrozenLocation() {

	return this.frozenloc;

    }

    public Playstyle getStyle() {
	return this.style;
    }

    public void setPlaystyle(Playstyle style) {

	this.style = style;
	this.player.sendMessage(ChatColor.DARK_GREEN + "You are a " + style.getName() + "!");

    }

    public HashMap<Ability, Long> getAbilitycooldown() {
	return abilitycooldown;
    }

    public void setAbilitycooldown(HashMap<Ability, Long> abilitycooldown) {
	this.abilitycooldown = abilitycooldown;
    }

    public double getBotspeed() {
	return botspeed;
    }

    public void setBotspeed(double botspeed) {
	this.botspeed = botspeed;
    }

    public double getBothealth() {
	return bothealth;
    }

    public void setBothealth(double bothealth) {
	this.bothealth = bothealth;
    }

    public int getChests() {
	return chests;
    }

    public void setChests(int chests) {
	this.chests = chests;
    }

    public int getPoints() {
	return points;
    }

    public void setPoints(int points) {
	this.points = points;
    }

    public Player getTradeinvite() {
	return tradeinvite;
    }

    public void setTradeinvite(Player tradeinvite) {
	this.tradeinvite = tradeinvite;
    }

    public ArrayList<Ability> getKit() {
	return kit;
    }

    public void setKit(ArrayList<Ability> kit) {
	this.kit = kit;
    }

    public void addAbility(Ability abi) {

	if (abi == null)
	    return;

	this.ownedAbilities.add(abi);
    }

    public boolean isBuild() {
	return build;
    }

    public void setBuild(boolean build) {
	this.build = build;
    }

    public boolean isAllowedtoseeplayers() {
	return allowedtoseeplayers;
    }

    public void setAllowedtoseeplayers(boolean allowedtoseeplayers) {
	this.allowedtoseeplayers = allowedtoseeplayers;
    }

    public Gamemode getGamemode() {
	return gamemode;
    }

    public void setGamemode(Gamemode gamemode) {
	this.gamemode = gamemode;
    }

    public HashMap<Integer, KitConfig> getKitconfigs() {
	return kitconfigs;
    }

    public void setKitconfigs(HashMap<Integer, KitConfig> kitconfigs) {
	this.kitconfigs = kitconfigs;
    }

}
