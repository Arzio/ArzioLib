package com.arzio.arziolib.api.wrapper.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.arzio.arziolib.api.util.CDLootType;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.Loot;
import com.arzio.arziolib.api.wrapper.LootChance;

import guava10.com.google.common.collect.BiMap;
import guava10.com.google.common.collect.HashBiMap;

public class LootImpl implements Loot{

	private final CDLootType type;
	private final Object instance;
	private final BiMap<Object, LootChanceImpl> lootContentInstanceMap = HashBiMap.create();
	
	public LootImpl(Object instance, CDLootType type) {
		this.type = type;
		this.instance = instance;
		this.mapAll();
	}
	
	private void mapAll() {
		lootContentInstanceMap.clear();
		for (Object o : CDClasses.lootTypeContent.getValue(instance)) {
			lootContentInstanceMap.put(o, new LootChanceImpl(o));
		}
	}
	
	@Override
	public CDLootType getType() {
		return type;
	}

	@Override
	public LootChance[] getChances() {
		return lootContentInstanceMap.values().toArray(new LootChance[0]);
	}

	@Override
	public void removeItem(Material material) {
		for (LootChance chance : getChances()) {
			if (chance.getItem() == material) {
				this.remove(chance);
			}
		}
	}

	@Override
	public void removeAll() {
		for (LootChance chance : this.getChances()) {
			this.remove(chance);
		}
	}
	
	@Override
	public void remove(LootChance loot) {
		Object lootInstance = lootContentInstanceMap.inverse().remove(loot);
		CDClasses.lootTypeContent.getValue(instance).remove(lootInstance);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void addChance(Material material, double chance) {
		try {
			Constructor<?> constructor = CDClasses.lootContentClass.getReferencedClass().getConstructor(int.class, double.class);
			Object newLootContent = constructor.newInstance(material.getId(), chance);
			
			CDClasses.lootTypeContent.getValue(instance).add(newLootContent);
			lootContentInstanceMap.put(newLootContent, new LootChanceImpl(newLootContent));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setSpawnFrequency(double frequency) {
		CDClasses.lootTypeSpawnChance.setValue(instance, frequency);
	}

	@Override
	public double getSpawnFrequency() {
		return CDClasses.lootTypeSpawnChance.getValue(instance);
	}

	@Override
	public LootChance[] getChances(Material material) {
		List<LootChance> chanceList = new ArrayList<>();
		for (LootChance chance : this.getChances()) {
			if (chance.getItem() == material) {
				chanceList.add(chance);
			}
		}
		
		return chanceList.toArray(new LootChance[0]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LootImpl other = (LootImpl) obj;
		if (type != other.type)
			return false;
		return true;
	}

}
