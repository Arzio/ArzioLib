package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.Material;

import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.Ammo;

public class AmmoImpl extends CDItemImpl implements Ammo{

	public AmmoImpl(Material material) {
		super(material);
	}

	@Override
	public double getPenetration() {
		return CDClasses.itemMagazinePenetrationField.getValue(this.getItemInstance());
	}

	@Override
	public void setPenetration(double value) {
		CDClasses.itemMagazinePenetrationField.setValue(this.getItemInstance(), value);
	}

	@Override
	public int getBulletAmount() {
		return CDClasses.itemMagazineBulletAmountField.getValue(this.getItemInstance());
	}

	@Override
	public void setBulletAmount(int amount) {
		CDClasses.itemMagazineBulletAmountField.setValue(this.getItemInstance(), amount);
	}

}
