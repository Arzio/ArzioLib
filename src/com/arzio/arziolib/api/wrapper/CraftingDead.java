package com.arzio.arziolib.api.wrapper;

import cpw.mods.fml.common.IPlayerTracker;

public interface CraftingDead {

	public IPlayerTracker getPlayerTracker();
	
	public void setTrackerEnabled(boolean enabled);
	
	public boolean isTrackerRegistered();
}
