package com.arzio.arziolib.api.wrapper;

import org.bukkit.Material;

import com.arzio.arziolib.api.util.CDAttachment;

public interface Gun extends CDItem {
	
	public int getBodyDamage();
	
	public void setBodyDamage(int value);
	
	public int getHeadshotDamage();
	
	public void setHeadshotDamage(int value);
	
	public Material[] getCompatibleAmmos();
	
	public void setCompatibleMagazines(Material... materials);
	
	public CDAttachment[] getCompatibleAttachments();
	
	public float getRPM();
	
	public long getMillisPerRound();
	
	public int getBulletsPerRound();
	
	public float getSoundLevel();
	
	public boolean isFireBased();
	
	public String getShootSound();
	
	public String getSilencedSound();
	
	public String getReloadSound();
	
	public void setShootSound(String soundName);
	
	public void setSilencedSound(String soundName);
	
	public void setReloadSound(String soundName);
}
