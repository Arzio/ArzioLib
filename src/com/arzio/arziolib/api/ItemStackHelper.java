package com.arzio.arziolib.api;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.api.impl.ItemInventoryNavigatorReturnable;
import com.arzio.arziolib.api.util.CDAttachment;
import com.arzio.arziolib.api.util.CDAttachmentType;


public interface ItemStackHelper {
	
	public boolean isContainer(ItemStack stack);
	
	public <T, R> Result<R> accessItemInventory(ItemInventoryNavigatorReturnable<R> applier);
	
	public void setGunAmmo(ItemStack stack, int amount);
	
	public int getGunAmmo(ItemStack stack);
	
	public boolean canGunFire(ItemStack stack);
	
	public Material getAttachment(ItemStack stack, CDAttachmentType type);
	
	public void setAttachment(ItemStack stack, CDAttachmentType type, CDAttachment attach);
	
	public boolean hasAttachment(ItemStack stack, CDAttachmentType type);
	
	public static interface InventoryNavigator<T> {
		public T accessAndReturn(Inventory inventory);
	}
	
	public static class Result<R> {
		
		private R result;
		private ItemStack stack;
		
		public Result(ItemStack stack, R result) {
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
