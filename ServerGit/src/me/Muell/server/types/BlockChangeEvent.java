package me.Muell.server.types;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class BlockChangeEvent extends BlockEvent {

    private static final HandlerList handlers = new HandlerList();

    public BlockChangeEvent(Block theBlock) {
	super(theBlock);
    }

    public static HandlerList getHandlerList() {
	return handlers;
    }

    public HandlerList getHandlers() {
	return handlers;
    }

}