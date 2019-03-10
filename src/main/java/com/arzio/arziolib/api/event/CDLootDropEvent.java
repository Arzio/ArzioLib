package com.arzio.arziolib.api.event;

import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.api.util.CauldronUtils;

import net.minecraft.server.v1_6_R3.DataWatcher;

public class CDLootDropEvent extends EntityEvent implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	private final Player who;
	
	public CDLootDropEvent(Player who, Entity what) {
		super(what);
		this.who = who;
	}
	
	public Player getPlayer() {
		return this.who;
	}
	
	public ItemStack getStack() {
		return CraftItemStack.asBukkitCopy(CauldronUtils.getNMSEntity(getEntity()).getDataWatcher().getItemStack(10));
	}
	
	public void setStack(ItemStack stack) {
		DataWatcher watcher = CauldronUtils.getNMSEntity(getEntity()).getDataWatcher();
		watcher.watch(10, CauldronUtils.getNMSStack(stack));
		watcher.h(10);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
