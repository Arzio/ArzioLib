package com.arzio.arziolib.api.impl;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class ItemInventoryNavigatorVoid extends ItemInventoryNavigator<Void>{
	
	public ItemInventoryNavigatorVoid(ItemStack stack) {
		super(stack);
	}
	
	public abstract void access(Inventory inventory);

	@Override
	public Void accessAndReturn(Inventory inventory) {
		this.access(inventory);
		return null;
	}

}
