package com.arzio.arziolib.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Evento que detecta quando o player irá dropar itens do inventario especial.
 * O inventario especial é o inventario do mod que tem mochila, colete, capacete e roupa.
 * @author Arzio
 *
 */
public class CDPlayerPreDropEvent extends Event{
	
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private boolean keepInventory = false;
	
	public CDPlayerPreDropEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public void setKeepInventory(boolean keepInventory) {
		this.keepInventory = keepInventory;
	}
	
	public boolean shouldKeepInventory() {
		return this.keepInventory;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
