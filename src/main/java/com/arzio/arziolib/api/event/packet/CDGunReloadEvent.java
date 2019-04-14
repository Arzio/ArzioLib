package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.arzio.arziolib.api.util.CDPacketDataWrapper;
import com.arzio.arziolib.api.wrapper.Gun;

public class CDGunReloadEvent extends GunEvent{

	private static final HandlerList handlers = new HandlerList();

	public CDGunReloadEvent(Player player, Gun gun, CDPacketDataWrapper dataWrapper) {
		super(player, gun, dataWrapper);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
