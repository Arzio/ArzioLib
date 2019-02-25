package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.arzio.arziolib.api.wrapper.Gun;

public class CDGunReloadEvent extends GunEvent{

	private static final HandlerList handlers = new HandlerList();
	private boolean spendAmmo = true;

	public CDGunReloadEvent(Player player, Gun gun, byte[] packetData) {
		super(player, gun, packetData);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public void setSpendAmmo(boolean willSpend) {
		this.spendAmmo = willSpend;
	}
	
	public boolean willSpendAmmo() {
		return this.spendAmmo;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
