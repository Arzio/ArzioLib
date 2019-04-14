package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

import com.arzio.arziolib.api.util.CDPacketDataWrapper;

public abstract class PayloadPacketEvent extends PlayerEvent implements Cancellable{

	private final CDPacketDataWrapper dataWrapper;
	private boolean cancelled;
	
	public PayloadPacketEvent(Player player, CDPacketDataWrapper dataWrapper) {
		super(player);
		this.dataWrapper = dataWrapper;
	}
	
	public boolean hasData() {
		return this.dataWrapper != null;
	}
	
	public CDPacketDataWrapper getData() {
		return this.dataWrapper;
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
