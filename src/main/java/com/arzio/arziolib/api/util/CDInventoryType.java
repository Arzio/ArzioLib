package com.arzio.arziolib.api.util;

import static com.arzio.arziolib.api.util.reflection.CDClasses.containerBackpackClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.containerFuelTanksClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.containerInventoryCDAClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.containerShelfLootClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.containerTacticalVestClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.containerVendingMachineClass;

import java.util.logging.Level;

import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftInventoryView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.reflection.ReflectedClass;

public enum CDInventoryType {
	PLAYER_INVENTORY(containerInventoryCDAClass),
	BACKPACK(containerBackpackClass),
	FUEL_TANK(containerFuelTanksClass),
	SHELF_LOOT(containerShelfLootClass),
	VEST(containerTacticalVestClass),
	VENDING_MACHINE(containerVendingMachineClass);
	
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
		
		return this.isTypeOf(view);
	}
	
	public boolean isTypeOf(InventoryView view) {
		if (!className.hasFound()) {
			ArzioLib.getInstance().getLogger().log(Level.SEVERE, "Class for InventoryType "+this.name()+" was not found!");
			return false;
		}
		
		if (view == null) {
			return false;
		}
		
		CraftInventoryView craftView = (CraftInventoryView) view;
		
		Class<?> viewClass = craftView.getHandle().getClass();
		
		return viewClass.equals(className.getReferencedClass());
	}
	
	public static boolean isViewingItemContainer(Player player) {
		return isItemContainer(player.getOpenInventory());
	}
	
	public static boolean isItemContainer(InventoryView view) {
		for (CDInventoryType type : ITEM_CONTAINERS) {
			if (type.isTypeOf(view)) {
				return true;
			}
		}
		return false;
	}
}
