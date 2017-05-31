package me.Muell.server.types;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class BlockUpdate {

    private Block block;
    private Material type;
    private byte data;
    private Location loc;
    private AtomicInteger timetoupdate;
    private BlockState state;
    private boolean withoutupdate;

    public BlockUpdate(Block block, Material type, byte data, int timetoupdate) {

	this.block = block;
	this.type = type;
	this.data = data;
	this.loc = block.getLocation();
	this.timetoupdate = new AtomicInteger(timetoupdate);

    }

    public Block getBlock() {
	return block;
    }

    public void setBlock(Block block) {
	this.block = block;
    }

    public Material getType() {
	return type;
    }

    public void setType(Material type) {
	this.type = type;
    }

    public byte getData() {
	return data;
    }

    public void setData(byte data) {
	this.data = data;
    }

    public Location getLoc() {
	return loc;
    }

    public void setLoc(Location loc) {
	this.loc = loc;
    }

    public AtomicInteger getTimetoupdate() {
	return timetoupdate;
    }

    public void setTimetoupdate(AtomicInteger timetoupdate) {
	this.timetoupdate = timetoupdate;
    }

    public BlockState getState() {
	return state;
    }

    public void setState(BlockState state) {
	this.state = state;
    }

    public boolean isWithoutupdate() {
	return withoutupdate;
    }

    public void setWithoutupdate(boolean withoutupdate) {
	this.withoutupdate = withoutupdate;
    }

}
