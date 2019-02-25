package com.arzio.arziolib.api.event;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityJoinWorldEvent extends EntityEvent implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private final World world;
	private boolean cancelled = false;
	
	public EntityJoinWorldEvent(Entity what, World world) {
		super(what);
		this.world = world;
	}
	
	public World getWorld() {
		return this.world;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
