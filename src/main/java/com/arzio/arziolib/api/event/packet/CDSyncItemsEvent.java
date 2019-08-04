package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.arzio.arziolib.api.util.CDPacketDataWrapper;

public class CDSyncItemsEvent extends PayloadPacketEvent{

	private static final HandlerList handlers = new HandlerList();

	public CDSyncItemsEvent(Player player, CDPacketDataWrapper dataWrapper) {
		super(player, dataWrapper);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}