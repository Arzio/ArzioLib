package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.api.exception.CDAReflectionException;
import com.arzio.arziolib.api.util.CDSpecialSlot;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.InventoryCDA;

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
			items[slot.getSlotIndex()] = CraftItemStack.asNMSCopy(item);
			
			if (slot.isOpenFor(player)) {
				player.closeInventory(); // Closes the inventory if the Item GUI is being viewed
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
