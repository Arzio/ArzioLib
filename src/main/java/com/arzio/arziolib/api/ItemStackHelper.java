package com.arzio.arziolib.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.awt.Color;

import com.arzio.arziolib.api.impl.ItemInventoryNavigator;
import com.arzio.arziolib.api.util.CDAttachment;
import com.arzio.arziolib.api.util.CDAttachmentType;
import com.arzio.arziolib.api.util.CDGunPaint;
import com.arzio.arziolib.api.util.CDSpecialSlot;
import com.arzio.arziolib.api.wrapper.InventoryCDA;


public interface ItemStackHelper {
	
	public boolean hasInventory(ItemStack stack);
	
	public <T, R> AccessResult<R> accessItemInventory(ItemInventoryNavigator<R> navigator);
	
	public <T, R> AccessResult<R> accessItemInventory(InventoryCDA inventory, CDSpecialSlot slot, InventoryNavigator<R> navigator);
	
	public void setGunAmmo(ItemStack gun, int amount);
	
	public int getGunAmmo(ItemStack gun);

	public void setGunPaint(ItemStack gun, CDGunPaint paint);

	public CDGunPaint getGunPaint(ItemStack gun);

	public void setGunColor(ItemStack gun, Color color);

	public Color getGunColor(ItemStack gun);
	
	public void setGunClip(ItemStack gun, ItemStack clip);
	
	public ItemStack getGunClip(ItemStack gun);
	
	public boolean canGunFire(ItemStack gun);
	
	public CDAttachment getAttachment(ItemStack gun, CDAttachmentType type);
	
	public void setAttachment(ItemStack gun, CDAttachmentType type, CDAttachment attach);
	
	public boolean hasAttachment(ItemStack gun, CDAttachmentType type);
	
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
