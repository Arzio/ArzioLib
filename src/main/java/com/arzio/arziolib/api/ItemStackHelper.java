package com.arzio.arziolib.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.api.impl.ItemInventoryNavigator;
import com.arzio.arziolib.api.util.CDAttachment;
import com.arzio.arziolib.api.util.CDAttachmentType;
import com.arzio.arziolib.api.util.CDSpecialSlot;
import com.arzio.arziolib.api.wrapper.InventoryCDA;


public interface ItemStackHelper {
	
	public boolean hasInventory(ItemStack stack);
	
	public <T, R> AccessResult<R> accessItemInventory(ItemInventoryNavigator<R> navigator);
	
	public <T, R> AccessResult<R> accessItemInventory(InventoryCDA inventory, CDSpecialSlot slot, InventoryNavigator<R> navigator);
	
	public void setGunAmmo(ItemStack stack, int amount);
	
	public int getGunAmmo(ItemStack stack);
	
	public void setGunClip(ItemStack gun, ItemStack clip);
	
	public ItemStack getGunClip(ItemStack gun);
	
	public boolean canGunFire(ItemStack stack);
	
	public CDAttachment getAttachment(ItemStack stack, CDAttachmentType type);
	
	public void setAttachment(ItemStack stack, CDAttachment attach);
	
	public boolean hasAttachment(ItemStack stack, CDAttachmentType type);
	
	public static interface InventoryNavigator<T> {
		public T accessAndReturn(Inventory inventory);
	}
	
	public static class AccessResult<R> {
		
		private R result;
		private ItemStack stack;
		
		public AccessResult(ItemStack stack, R result) {
			this.result = result;
			this.stack = stack;
		}
		
		public R getResult() {
			return this.result;
		}
		
		public ItemStack getStack() {
			return this.stack;
		}
	}
}
