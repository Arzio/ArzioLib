package com.arzio.arziolib.api.wrapper;

import org.bukkit.Material;

import com.arzio.arziolib.api.util.CDAttachment;

public interface Gun extends CDSharedItem {
	
	public int getBodyDamage();
	public void setBodyDamage(int value);
	
	public int getHeadshotDamage();
	public void setHeadshotDamage(int value);
	
	public Material[] getCompatibleAmmos();
	public void setCompatibleMagazines(Material... materials);
	
	public CDAttachment[] getCompatibleAttachments();
	
	public double getRPM();
	public void setRPM(double rpm);
	
	public long getMillisPerRound();
	
	public int getBulletsPerRound();
	public void setBulletsPerRound(int amount);
	
	public int getMaximumBulletDistance();
	public void setMaximumBulletDistance(int distance);
	
	public double getRecoil();
	public void setRecoil(double recoil);
	
	public double getAccuracy();
	public void setAccuracy(double accuracy);
	
	public double getMovementPenalty();
	public void setMovementPenalty(double amount);
	
	public double getZoomLevel();
	public void setZoomLevel(double amount);
	
	public double getSoundLevel();
	public void setSoundLevel(double soundLevel);
	
	public boolean isFireBased();
	
	public boolean canSpreadWhileAiming();
	public void setSpreadWhileAiming(boolean canSpread);
	
	public boolean hasCrosshair();
	public void setCrosshair(boolean hasCrosshair);
	
    public String getShootSound();
	public void setShootSound(String soundName);
	
	public String getSilencedSound();
	public void setSilencedSound(String soundName);
	
	public String getReloadSound();
	public void setReloadSound(String soundName);
}
