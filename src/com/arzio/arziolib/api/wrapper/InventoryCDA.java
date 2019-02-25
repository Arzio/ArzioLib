package com.arzio.arziolib.api.wrapper;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.api.util.CDSpecialSlot;

public interface InventoryCDA {
	/**
	 * Gets the stack in a special slot of the player.
	 * @param player The player
	 * @param slot Special slot
	 * @return
	 */
	public ItemStack getStackInSpecialSlot(Player player, CDSpecialSlot slot);
	
	/**
	 * Sets the stack in a special slot of the player.
	 * @param player The player
	 * @param slot Special slot
	 * @param item Stack to apply
	 */
	public void setStackInSpecialSlot(Player player, CDSpecialSlot slot, ItemStack item);
	
	/**
	 * Clears all the special slots of the player.
	 * @param player The player
	 */
	public void clearSpecialSlots(Player player);
}
