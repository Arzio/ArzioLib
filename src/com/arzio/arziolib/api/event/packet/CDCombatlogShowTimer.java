package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class CDCombatlogShowTimer extends PayloadPacketEvent{

	private static final HandlerList handlers = new HandlerList();
	
	public CDCombatlogShowTimer(Player player, byte[] packetData) {
		super(player, packetData);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
