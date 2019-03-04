package com.arzio.arziolib.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.arzio.arziolib.api.bases.Base;

public class CDBaseLoadEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	private final Base base;

	public CDBaseLoadEvent(Base base) {
		this.base = base;
	}

	public Base getBase() {
		return this.base;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
