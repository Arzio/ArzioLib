package com.arzio.arziolib.api.util;

import org.bukkit.block.Block;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.wrapper.Loot;

public enum CDLootType {

	RESIDENTIAL("residential", CDMaterial.RESIDENTIAL_LOOT),
	RESIDENTIAL_RARE("residentialrare", CDMaterial.RARE_RESIDENTIAL_LOOT),
	MEDICAL("medical", CDMaterial.MEDICAL_LOOT),
	MILITARY("military", CDMaterial.MILITARY_LOOT),
	POLICE("police", CDMaterial.POLICE_LOOT);
	
	private String id;
	private CDMaterial material;
	
	private CDLootType(String id, CDMaterial material) {
		this.id = id;
	}
	
	public CDMaterial getMaterial() {
		return this.material;
	}
	
	public String getId() {
		return this.id;
	}
	
	public Loot getLoot() {
		return ArzioLib.getInstance().getLootProvider().getLoot(this);
	}
	
	public static CDLootType getFrom(Block block) {
		for (CDLootType lootType : CDLootType.values()) {
			if (block.getType() == lootType.getMaterial().asMaterial()) {
				return lootType;
			}
		}
		return null;
	}

}
