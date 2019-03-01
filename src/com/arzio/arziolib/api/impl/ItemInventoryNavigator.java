package com.arzio.arziolib.api.impl;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.api.ItemStackHelper.InventoryNavigator;

public abstract class ItemInventoryNavigator<T> implements InventoryNavigator<T>{

	private ItemStack itemStack;
	
	public ItemInventoryNavigator(ItemStack stack) {
		Validate.notNull(stack, "ItemStack cannot be null!");
		this.itemStack = stack;
	}
	
	public ItemStack getItemStack() {
		return this.itemStack;
	}

}
