package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class CDPlayerDataSendEvent extends PayloadPacketEvent{

	private static final HandlerList handlers = new HandlerList();
	private final Player from;
	
	public CDPlayerDataSendEvent(Player from, Player to, byte[] packetData) {
		super(to, packetData);
		this.from = from;
	}
	
	public Player getFrom() {
		return this.from;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
