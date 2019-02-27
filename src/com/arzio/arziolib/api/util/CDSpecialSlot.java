package com.arzio.arziolib.api.util;

import org.bukkit.entity.Player;

public enum CDSpecialSlot {
	
	GUN("gun", 0),
	BACKPACK("backpack", 1, CDInventoryType.BACKPACK, CDInventoryType.FUEL_TANK),
	VEST("vest", 2, CDInventoryType.VEST),
	MELEE("melee", 3),
	HAT("hat", 4),
	CLOTHING("clothing", 5),
	C_GUN("cgun", 6),
	C_ATTACH1("cattach1", 7),
	C_ATTACH2("cattach2", 8),
	C_ATTACH3("cattach3", 9),
	C_PAINT("cpaint", 10);
	
	private String id;
	private int index;
	private CDInventoryType[] possibleInventoryTypes;
	
	CDSpecialSlot(String id, int slotIndex, CDInventoryType... possibleInventoryTypes){
		this(id, slotIndex);
		this.possibleInventoryTypes = possibleInventoryTypes;
	}
	
	CDSpecialSlot(String id, int slotIndex){
		this.id = id;
		this.index = slotIndex;
	}
	
	public boolean isOpenFor(Player player) {
		for (CDInventoryType type : possibleInventoryTypes) {
			if (type.isOpenFor(player)) {
				return true;
			}
		}
		return false;
	}
	
	public int getSlotIndex() {
		return this.index;
	}
	
	public String getId() {
		return this.id;
	}
}
