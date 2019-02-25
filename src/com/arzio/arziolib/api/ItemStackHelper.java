package com.arzio.arziolib.api;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.api.util.CDAttachment;
import com.arzio.arziolib.api.util.CDAttachmentType;


public interface ItemStackHelper {
	
	/**
	 * Gets every ItemStack inside this ItemStack.
	 * This is intended to be used in backpacks and vests.
	 * @param stack Item stack
	 * @return List of itemstacks
	 */
	public List<ItemStack> getStacksFromBag(ItemStack stack);
	
	public void setAmmoInGun(ItemStack stack, int amount);
	
	public int getAmmoInGun(ItemStack stack);
	
	public boolean canGunFire(ItemStack stack);
	
	public Material getAttachment(ItemStack stack, CDAttachmentType type);
	
	public void setAttachment(ItemStack stack, CDAttachmentType type, CDAttachment attach);
	
	public boolean hasAttachment(ItemStack stack, CDAttachmentType type);
}
