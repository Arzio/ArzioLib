package com.arzio.arziolib.api.wrapper;

import org.bukkit.Material;

public interface Gun extends CDItem {
	
	public int getBodyDamage();
	
	public void setBodyDamage(int value);
	
	public int getHeadshotDamage();
	
	public void setHeadshotDamage(int value);
	
	public Material[] getCompatibleAmmos();
	
	public void setCompatibleMagazines(Material... materials);
	
	public float getRPM();
	
	public long getMillisPerRound();
	
	public int getBulletsPerRound();
	
	public float getSoundLevel();
	
	public boolean isFireBased();
	
	public String getShootSound();
	
	public String getSilencedSound();
	
	public void setShootSound(String soundName);
	
	public void setSilencedSound(String soundName);
}
