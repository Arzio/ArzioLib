package com.arzio.arziolib.api.wrapper;

import org.bukkit.Material;

import com.arzio.arziolib.api.util.CDLootType;

public interface Loot {

	public CDLootType getType();
	
	public LootChance[] getChances();
	
	public void remove(LootChance loot);
	
	public void removeItem(Material material);
	
	public void removeAll();
	
	public void setSpawnFrequency(double frequency);
	
	public double getSpawnFrequency();
	
	public LootChance[] getChances(Material material);
	
	public void addChance(Material material, double chance);
}
