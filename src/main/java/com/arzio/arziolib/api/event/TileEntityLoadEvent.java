package com.arzio.arziolib.api.event;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

import net.minecraft.server.v1_6_R3.TileEntity;

public class TileEntityLoadEvent extends TileEntityEvent {

	private static final HandlerList handlers = new HandlerList();
	
	public TileEntityLoadEvent(Block block, TileEntity tileEntity, World world) {
		super(block, tileEntity, world);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
