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
	
	/* TODO :3
	public boolean isHandcuffed();
	
	public void setHandcuffed(boolean status);
	
	public int getHandcuffDamage();
	
	public void setHandcuffDamage(int value);
	
	public int getWaterLevel();
	
	public void setWaterLevel(int amount);
	
	public int getBloodLevel();
	
	public void setBloodLevel(int amount);
	*/
	
	public void setCanViewNameTags(boolean isPossible);
}
