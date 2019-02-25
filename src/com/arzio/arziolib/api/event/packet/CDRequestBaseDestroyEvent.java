package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.arzio.arziolib.api.bases.Base;

public class CDRequestBaseDestroyEvent extends PayloadPacketEvent{

	private static final HandlerList handlers = new HandlerList();
	private final Base base;

	public CDRequestBaseDestroyEvent(Player player, Base base, byte[] packetData) {
		super(player, packetData);
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
