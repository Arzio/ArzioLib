package com.arzio.arziolib.api.impl;

import org.bukkit.inventory.Inventory;

import com.arzio.arziolib.api.ItemStackHelper.InventoryNavigator;

public abstract class InventoryNavigatorVoid implements InventoryNavigator<Void>{
	
	public abstract void access(Inventory inventory);

	@Override
	public Void accessAndReturn(Inventory inventory) {
		this.access(inventory);
		return null;
	}
}
