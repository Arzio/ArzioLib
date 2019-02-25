package com.arzio.arziolib.api.util;

public enum CDSpecialSlot {
	
	GUN("gun", 0),
	BACKPACK("backpack", 1),
	VEST("vest", 2),
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
	
	CDSpecialSlot(String id, int slotIndex){
		this.id = id;
		this.index = slotIndex;
	}
	
	public int getSlotIndex() {
		return this.index;
	}
	
	public String getId() {
		return this.id;
	}
}
