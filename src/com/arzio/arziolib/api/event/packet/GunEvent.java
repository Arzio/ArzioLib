package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;

import com.arzio.arziolib.api.wrapper.Gun;

public abstract class GunEvent extends PayloadPacketEvent{
	
	private final Gun gun;

	public GunEvent(Player player, Gun gun, byte[] packetData) {
		super(player, packetData);
		this.gun = gun;
	}

	public Gun getHeldGun() {
		return this.gun;
	}

}
