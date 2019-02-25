package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

public abstract class PayloadPacketEvent extends PlayerEvent implements Cancellable{

	private byte[] packetData;
	private boolean cancelled;
	
	public PayloadPacketEvent(Player player, byte[] packetData) {
		super(player);
		this.packetData = packetData;
	}
	
	public byte[] getData() {
		return this.packetData;
	}
	
	public void setData(byte[] data) {
		this.packetData = data;
	}
	
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
