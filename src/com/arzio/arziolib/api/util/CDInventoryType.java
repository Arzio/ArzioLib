package com.arzio.arziolib.api.util;

import static com.arzio.arziolib.api.util.reflection.CDClasses.inventoryBackpackClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.inventoryCDAClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.inventoryFuelTanksClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.inventoryShelfLootClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.inventoryTacticalVestClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.inventoryVendingMachineClass;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.reflection.ReflectedClass;

import net.minecraft.server.v1_6_R3.IInventory;

public enum CDInventoryType {
	PLAYER_INVENTORY(inventoryCDAClass),
	BACKPACK(inventoryBackpackClass),
	FUEL_TANK(inventoryFuelTanksClass),
	SHELF_LOOT(inventoryShelfLootClass),
	VEST(inventoryTacticalVestClass),
	VENDING_MACHINE(inventoryVendingMachineClass);
	
	public static final CDInventoryType[] ITEM_CONTAINERS = new CDInventoryType[] { BACKPACK, FUEL_TANK, VEST };
	
	private ReflectedClass className;
	
	private CDInventoryType(ReflectedClass inventoryClass) {
		this.className = inventoryClass;
	}
	
	public boolean isOpenFor(Player player) {
		InventoryView view = player.getOpenInventory();
		
		if (view == null) {
			return false;
		}
		
		Inventory topInventory = view.getTopInventory();
		
		if (topInventory == null) {
			return false;
		}
		
		return this.isTypeOf(topInventory);
	}
	
	public boolean isTypeOf(Inventory inventory) {
		if (!className.hasFound()) {
			ArzioLib.getInstance().getLogger().log(Level.SEVERE, "Class for InventoryType "+this.name()+" was not found!");
			return false;
		}
		
		IInventory nmsInventory = CauldronUtils.getNMSInventory(inventory);
		
		if (nmsInventory == null) {
			return false;
		}
		
		return nmsInventory.getClass().equals(className.getReferencedClass());
	}
	
	public static boolean isItemContainer(Inventory inventory) {
		for (CDInventoryType type : ITEM_CONTAINERS) {
			if (type.isTypeOf(inventory)) {
				return true;
			}
		}
		return false;
	}
}
