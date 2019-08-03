package com.arzio.arziolib.api.event;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

import net.minecraft.server.v1_6_R3.TileEntity;

public abstract class TileEntityEvent extends BlockEvent {

	private static final HandlerList handlers = new HandlerList();
	private final World world;
	private final BlockState blockState;
	private final TileEntity tileEntity;
	
	public TileEntityEvent(Block block, TileEntity tileEntity, World world) {
		super(block);
		this.blockState = block.getState();
		this.world = world;
		this.tileEntity = tileEntity;

		Validate.notNull(this.blockState, "BlockState of a TileEntity must not be null!");
	}
	
	public World getWorld() {
		return this.world;
	}

	public BlockState getState(){
		return this.blockState;
	}

	public TileEntity getTileEntity(){
		return this.tileEntity;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
