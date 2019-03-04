package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;

import com.arzio.arziolib.api.util.CDPacketDataWrapper;
import com.arzio.arziolib.api.wrapper.Gun;

public abstract class GunEvent extends PayloadPacketEvent{
	
	private final Gun gun;

	public GunEvent(Player player, Gun gun, CDPacketDataWrapper dataWrapper) {
		super(player, dataWrapper);
		this.gun = gun;
	}

	public Gun getHeldGun() {
		return this.gun;
	}

}
