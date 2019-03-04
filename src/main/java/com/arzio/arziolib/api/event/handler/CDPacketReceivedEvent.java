package com.arzio.arziolib.api.event.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.arzio.arziolib.api.util.CDPacketDataWrapper;

public class CDPacketReceivedEvent extends PacketHandlerEvent{
	
	private static final HandlerList handlers = new HandlerList();
	
	public CDPacketReceivedEvent(Player sender, CDPacketDataWrapper dataWrapper) {
		super(sender, dataWrapper);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
