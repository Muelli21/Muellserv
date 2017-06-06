package me.Muell.server.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_7_R4.WorldServer;

public class Hologram {

    public static HashSet<Hologram> allHolograms = new HashSet<>();
    public static HashMap<String, Hologram> holograms = new HashMap<>();

    private Location loc;
    private ArrayList<EntityHorse> horses = new ArrayList<>();
    private HashMap<EntityHorse, EntityWitherSkull> skulls = new HashMap<>();
    private String name;
    private ArrayList<String> linestext = new ArrayList<>();

    private HashSet<Player> playersshowed = new HashSet<>();

    public Hologram(Location loc, String name) {
	this.loc = loc;
	this.name = name;
	holograms.put(name, this);
	allHolograms.add(this);
    }

    public void set(ArrayList<String> text) {

	this.linestext = text;
	int linessofar = 0;

	for (String textline : linestext) {

	    linessofar++;
	    createHorse(linessofar, textline);
	}
    }

    @SuppressWarnings("deprecation")
    public void show() {

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    if (playersshowed.contains(ps))
		sendNamePackets(ps);

	    if (!playersshowed.contains(ps))
		sendSpawnPackets(ps);

	    this.playersshowed.add(ps);
	}
    }

    public void show(Player p) {

	if (playersshowed.contains(p))
	    sendNamePackets(p);

	if (!playersshowed.contains(p))
	    sendSpawnPackets(p);

	this.playersshowed.add(p);

    }

    public void showPlayersShowed() {

	for (Player ps : playersshowed) {

	    sendNamePackets(ps);
	}
    }

    public void showAfterDeath(Player p) {

	sendSpawnPackets(p);
    }

    public void update(ArrayList<String> text) {
	this.linestext = text;

	int linessofar = 0;

	for (String textline : linestext) {

	    if (horses.size() < text.size()) {
		createHorse(text.size(), textline);

		for (Player ps : playersshowed)
		    sendSpawnPackets(ps);
	    }

	    EntityHorse horse = horses.get(linessofar);
	    horse.setCustomName(textline);

	    linessofar++;
	}

	showPlayersShowed();
    }

    @SuppressWarnings("deprecation")
    public void destroy() {

	for (EntityHorse horse : horses) {

	    EntityWitherSkull skull = skulls.get(horse);

	    for (Player ps : Bukkit.getOnlinePlayers()) {
		EntityPlayer nmsPlayer = ((CraftPlayer) ps).getHandle();
		PacketPlayOutEntityDestroy horsedestroy = new PacketPlayOutEntityDestroy(horse.getId());
		PacketPlayOutEntityDestroy skulldestroy = new PacketPlayOutEntityDestroy(skull.getId());
		nmsPlayer.playerConnection.sendPacket(horsedestroy);
		nmsPlayer.playerConnection.sendPacket(skulldestroy);
	    }
	}

	setHorses(null);
	playersshowed = null;
	skulls.clear();
	getLinestext().clear();
	allHolograms.remove(this);

	if (name != null) {
	    holograms.remove(name);
	    name = null;
	}
    }

    public void createHorse(int linessofar, String textline) {

	WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();

	Location spawnloc = loc.clone().add(0, 54.56, 0).subtract(0, 0.23 * linessofar + 0.23, 0);

	EntityHorse horse = new EntityHorse(world);
	horse.setLocation(spawnloc.getX(), spawnloc.getY(), spawnloc.getZ(), 0, 0);
	horse.setAge(-1700000);
	horse.setCustomNameVisible(true);
	horse.setCustomName(textline);

	EntityWitherSkull skull = new EntityWitherSkull(world);
	skull.setLocation(spawnloc.getX(), spawnloc.getY(), spawnloc.getZ(), 0, 0);

	this.horses.add(horse);
	this.skulls.put(horse, skull);
    }

    private void sendSpawnPackets(Player target) {

	EntityPlayer nmsPlayer = ((CraftPlayer) target).getHandle();

	for (EntityHorse horse : horses) {

	    EntityWitherSkull skull = skulls.get(horse);

	    PacketPlayOutSpawnEntityLiving horsepacket = new PacketPlayOutSpawnEntityLiving(horse);
	    PacketPlayOutSpawnEntity skullpacket = new PacketPlayOutSpawnEntity(skull, 66);

	    nmsPlayer.playerConnection.sendPacket(horsepacket);
	    nmsPlayer.playerConnection.sendPacket(skullpacket);

	    PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse, skull);
	    nmsPlayer.playerConnection.sendPacket(pa);
	}
    }

    private void sendNamePackets(Player target) {

	EntityPlayer nmsPlayer = ((CraftPlayer) target).getHandle();

	for (EntityHorse horse : horses) {

	    PacketPlayOutEntityMetadata horsepacket = new PacketPlayOutEntityMetadata(horse.getId(), horse.getDataWatcher(), true);
	    nmsPlayer.playerConnection.sendPacket(horsepacket);
	}
    }

    public void updatePosition(Location loc) {

	for (EntityWitherSkull skull : skulls.values()) {

	    if (!getLoc().getWorld().equals(loc.getWorld()))
		return;

	    Location move = loc.clone().subtract(getLoc());
	    double x = skull.as.a(move.getX());
	    double y = skull.as.a(move.getY());
	    double z = skull.as.a(move.getZ());

	    PacketPlayOutRelEntityMove skullmovepacket = new PacketPlayOutRelEntityMove(skull.getId(), (byte) x, (byte) y, (byte) z, true);

	    for (Player ps : playersshowed) {

		EntityPlayer nmsPlayer = ((CraftPlayer) ps).getHandle();
		nmsPlayer.playerConnection.sendPacket(skullmovepacket);
	    }
	}

	this.loc = loc;
    }

    public ArrayList<EntityHorse> getHorses() {
	return horses;
    }

    public void setHorses(ArrayList<EntityHorse> horses) {
	this.horses = horses;
    }

    public Location getLoc() {
	return loc;
    }

    public void setLoc(Location loc) {
	this.loc = loc;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public ArrayList<String> getLinestext() {
	return linestext;
    }

    public Hologram(Location loc) {
	this.loc = loc;
    }
}
