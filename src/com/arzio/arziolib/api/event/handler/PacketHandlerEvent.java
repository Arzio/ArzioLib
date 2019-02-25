package com.arzio.arziolib.api.event.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import com.arzio.arziolib.api.event.packet.PayloadPacketEvent;
import com.arzio.arziolib.api.util.CDPacketType;

public abstract class PacketHandlerEvent extends PayloadPacketEvent implements Cancellable{
	
	private boolean cancelled;
	private final int packetId;
	
	public PacketHandlerEvent(Player sender, int packetId, byte[] data) {
		super(sender, data);
		this.packetId = packetId;
	}
	
	public CDPacketType getPacketType() {
		return CDPacketType.getById(this.packetId);
	}
	
	public int getPacketId() {
		return this.packetId;
	}
	
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	public void callInnerPacket(PayloadPacketEvent innerEvent) {
		Bukkit.getPluginManager().callEvent(innerEvent);
		if (innerEvent.isCancelled()) {
			this.setCancelled(true);
		} else {
			// As the event is not cancelled, we put every data modification into the packet.
			if (innerEvent instanceof PayloadPacketEvent) {
				PayloadPacketEvent dataEvent = (PayloadPacketEvent) innerEvent;
				this.setData(dataEvent.getData());
			}
		}
	}
}
