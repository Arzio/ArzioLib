package com.arzio.arziolib.api.wrapper.impl;

import java.lang.reflect.Field;

import org.bukkit.Material;

import com.arzio.arziolib.api.exception.CDAReflectionException;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.Ammo;

public class AmmoImpl extends CDSharedItemImpl implements Ammo{

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
		
		try {
		    Field durabilityField = net.minecraft.server.v1_6_R3.Item.class.getDeclaredField("durability");
		    durabilityField.setAccessible(true);
		    durabilityField.setInt(this.getItemInstance(), amount + 1);
		} catch (Exception e) {
		    throw new CDAReflectionException(e);
		}
		
	}

}
