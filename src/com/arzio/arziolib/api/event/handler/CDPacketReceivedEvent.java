package com.arzio.arziolib.api.event.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class CDPacketReceivedEvent extends PacketHandlerEvent{
	
	private static final HandlerList handlers = new HandlerList();
	
	public CDPacketReceivedEvent(Player sender, int packetId, byte[] data) {
		super(sender, packetId, data);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
