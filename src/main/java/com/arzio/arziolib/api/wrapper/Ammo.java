package com.arzio.arziolib.api.wrapper;

public interface Ammo extends CDSharedItem {

	public double getPenetration();
	
	public void setPenetration(double value);
	
	public int getBulletAmount();
	
	public void setBulletAmount(int amount);
}
