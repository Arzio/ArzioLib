package com.arzio.arziolib.api.wrapper;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PlayerData {

	public String getPlayerName();
	public Player getPlayer();
	
	public boolean hasBase();
	public Location getBaseLocation();
	public int getBaseX();
	public int getBaseY();
	public int getBaseZ();
	
	public InventoryCDA getInventory();
	public Base getBase();
	
	public void setNametagsHidden(boolean isPossible);
	public boolean isNametagsHidden();
	public void resendViewableNametags();
	
	public int getMaxWaterLevel();
	public int getWaterLevel();
	public void setWaterLevel(int amount);

	public boolean isAiming();
	
}
