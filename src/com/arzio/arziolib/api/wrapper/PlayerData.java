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
	
	public void setCanViewNameTags(boolean isPossible);
}
