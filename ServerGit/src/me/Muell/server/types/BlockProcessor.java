package me.Muell.server.types;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.Muell.server.Main;

public class BlockProcessor {

    public static BlockProcessor instance;

    private int blockspertick = 300;
    private List<BlockUpdate> queue = new ArrayList<>();
    private HashMap<BlockUpdate, AtomicInteger> blocksqueue = new HashMap<>();
    private BukkitTask task;

    public static BlockProcessor inst() {

	if (instance == null) {
	    BlockProcessor processor = new BlockProcessor();
	    instance = processor;
	}

	return instance;
    }

    public BlockProcessor() {

	BukkitTask task = new BukkitRunnable() {

	    int updates = 0;

	    @SuppressWarnings("deprecation")
	    @Override
	    public void run() {

		if (queue.isEmpty()) {
		    stop();
		    return;
		}

		for (BlockUpdate update : new ArrayList<>(queue)) {

		    if (updates > blockspertick)
			continue;

		    AtomicInteger aint = blocksqueue.get(update);

		    if (aint.decrementAndGet() <= 0) {

			updates++;

			Location loc = update.getLoc();
			Block futureblock = loc.getWorld().getBlockAt(loc);
			futureblock.setType(update.getType());
			futureblock.setData(update.getData());

//			if (update.isWithoutupdate())
//			    update.getState().update(true, false);

			blockChange(futureblock);

			queue.remove(update);
			updates--;
		    }
		}
	    }
	}.runTaskTimer(Main.getPlugin(), 0, 1);

	this.task = task;
    }

    public void addFuture(Block block, Material type, byte data, int timetoupdate, boolean withoutupdate) {

	BlockUpdate update = new BlockUpdate(block, type, data, timetoupdate);
	update.setWithoutupdate(withoutupdate);
	queue.add(update);
	blocksqueue.put(update, update.getTimetoupdate());
	update.setState(block.getState());
    }

    public void add(Block block, Material type, byte data, boolean withoutupdate) {

	BlockUpdate update = new BlockUpdate(block, type, data, 0);
	update.setWithoutupdate(withoutupdate);
	queue.add(update);
	blocksqueue.put(update, update.getTimetoupdate());
	update.setState(block.getState());
    }

    public void stop() {

	getTask().cancel();
	queue.clear();
	blocksqueue.clear();
	instance = null;
	return;
    }

    public int getBlockspertick() {
	return blockspertick;
    }

    public void setBlockspertick(int blockspertick) {
	this.blockspertick = blockspertick;
    }

    public List<BlockUpdate> getQueue() {
	return queue;
    }

    public void setQueue(List<BlockUpdate> queue) {
	this.queue = queue;
    }

    public HashMap<BlockUpdate, AtomicInteger> getBlocksqueue() {
	return blocksqueue;
    }

    public void setBlocksqueue(HashMap<BlockUpdate, AtomicInteger> blocksqueue) {
	this.blocksqueue = blocksqueue;
    }

    public BukkitTask getTask() {
	return task;
    }

    public void setTask(BukkitTask task) {
	this.task = task;
    }

    public void blockChange(Block block) {

	BlockChangeEvent event = new BlockChangeEvent(block);
	Bukkit.getPluginManager().callEvent(event);

    }

}
