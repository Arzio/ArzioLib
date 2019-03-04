package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.Material;

import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.LootChance;

public class LootChanceImpl implements LootChance {

	private Object lootContentInstance;
	
	public LootChanceImpl(Object lootContentInstance) {
		this.lootContentInstance = lootContentInstance;
	}
	
	@Override
	public double getChance() {
		return CDClasses.lootContentChance.getValue(lootContentInstance);
	}
	
	public Object getLootContentInstance() {
		return this.lootContentInstance;
	}

	@Override
	public void setChance(double chance) {
		CDClasses.lootContentChance.setValue(lootContentInstance, chance);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Material getItem() {
		return Material.getMaterial(CDClasses.lootContentItemId.getValue(lootContentInstance));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setItem(Material item) {
		CDClasses.lootContentItemId.setValue(lootContentInstance, item.getId());
	}

}
