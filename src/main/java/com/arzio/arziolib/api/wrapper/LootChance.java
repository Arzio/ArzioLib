package com.arzio.arziolib.api.wrapper;

import org.bukkit.Material;

public interface LootChance {

	public double getChance();
	public void setChance(double chance);
	
	public Material getItem();
	public void setItem(Material item);
}
