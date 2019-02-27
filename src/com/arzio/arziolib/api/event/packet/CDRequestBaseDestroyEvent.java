package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.arzio.arziolib.api.bases.Base;
import com.arzio.arziolib.api.util.CDPacketDataWrapper;

public class CDRequestBaseDestroyEvent extends PayloadPacketEvent{

	private static final HandlerList handlers = new HandlerList();
	private final Base base;

	public CDRequestBaseDestroyEvent(Player player, Base base, CDPacketDataWrapper dataWrapper) {
		super(player, dataWrapper);
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
