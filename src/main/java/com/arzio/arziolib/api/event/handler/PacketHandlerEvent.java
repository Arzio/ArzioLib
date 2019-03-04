package com.arzio.arziolib.api.event.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import com.arzio.arziolib.api.event.packet.PayloadPacketEvent;
import com.arzio.arziolib.api.util.CDPacketDataWrapper;
import com.arzio.arziolib.api.util.CDPacketType;

public abstract class PacketHandlerEvent extends PayloadPacketEvent implements Cancellable{
	
	private boolean cancelled;
	
	public PacketHandlerEvent(Player sender, CDPacketDataWrapper dataWrapper) {
		super(sender, dataWrapper);
	}
	
	public CDPacketType getPacketType() {
		return CDPacketType.getById(this.getData().getInnerPacketId());
	}
	
	public int getPacketId() {
		return this.getData().getInnerPacketId();
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
