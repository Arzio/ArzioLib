package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.api.exception.CDAReflectionException;
import com.arzio.arziolib.api.util.CDSpecialSlot;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.InventoryCDA;

import net.minecraft.server.v1_6_R3.NBTTagCompound;

public class InventoryCDAImpl implements InventoryCDA{
	
	public final PlayerDataImpl playerData;
	
	public InventoryCDAImpl(PlayerDataImpl data) {
		this.playerData = data;
	}

	@Override
	public void clearSpecialSlots() {
		
		for (CDSpecialSlot slot : CDSpecialSlot.values()) {
			this.setStackInSpecialSlot(slot, null);
		}

	}

	@Override
	public void setStackInSpecialSlot(CDSpecialSlot slot, ItemStack item) {
		try {
			Player player = playerData.getPlayer();
			
			net.minecraft.server.v1_6_R3.ItemStack[] items = CDClasses.inventoryCDAInventory.getValue(
					CDClasses.playerDataInventoryCDA.getValue(playerData.getPlayerDataInstance()));
			
			net.minecraft.server.v1_6_R3.ItemStack oldStack = items[slot.getSlotIndex()];
			items[slot.getSlotIndex()] = CraftItemStack.asNMSCopy(item);
			
			if (slot.isOpenFor(player)) {
				NBTTagCompound newStackCompound = CauldronUtils.getTagCompound(item);
				
				if (newStackCompound != null && oldStack != null && newStackCompound.equals(oldStack.tag)) {
					return;
				}

				// Closes the inventory if the Item GUI is being viewed,
				// AND if the items are NOT equal.
				player.closeInventory();
			}
		} catch (Exception e) {
			throw new CDAReflectionException(e);
		}
	}

	@Override
	public ItemStack getStackInSpecialSlot(CDSpecialSlot slot) {
		try {
			net.minecraft.server.v1_6_R3.ItemStack[] items = CDClasses.inventoryCDAInventory.getValue(CDClasses.playerDataInventoryCDA.getValue(playerData.getPlayerDataInstance()));
			return CraftItemStack.asBukkitCopy(items[slot.getSlotIndex()]);
		} catch (Exception e) {
			throw new CDAReflectionException(e);
		}
	}

}
