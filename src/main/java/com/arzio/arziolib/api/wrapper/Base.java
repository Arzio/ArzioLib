package com.arzio.arziolib.api.wrapper;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface Base {

	public String getOwnerName();
	public Player getOwner();
	public boolean isValid();
	public void destroy();
	public boolean isPartOfBase(Block block);
	public boolean isInsideBase(Location location);
	public Location getLocation();
	public int getTimeIdle();
	public void setTimeIdle(int time);
	public String[] getMembers();
	public void setMembers(String[] members);
	/*
	public Set<Block> getBlocks();
	*/

}
