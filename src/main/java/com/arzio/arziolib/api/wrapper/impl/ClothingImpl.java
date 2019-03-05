package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.Material;

import com.arzio.arziolib.api.util.CDProtectionLevel;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.Clothing;

public class ClothingImpl extends CDItemImpl implements Clothing{

	public ClothingImpl(Material material) {
		super(material);
	}

	@Override
	public CDProtectionLevel getProtectionLevel() {
		return CDProtectionLevel.getFromLevel(CDClasses.itemClothingProtectionLevel.getValue(this.getItemInstance()));
	}

	@Override
	public void setProtectionLevel(CDProtectionLevel level) {
		CDClasses.itemClothingProtectionLevel.setValue(this.getItemInstance(), level.getLevel());
	}

}
