package com.arzio.arziolib.api.bases;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public interface BaseProvider {

	public Base getBaseFromCenter(BlockState state);
	public Base getBaseFromCenter(Block block);
	public Base getBaseFromCenter(Location location);
	
	public Base getBaseFromPlayer(Player player);
	public Base getBaseFromPart(Block block);
	
	public boolean isPartOfAnyBase(Block block);
	public boolean hasBase(Player player);
	
	public Set<Base> getLoadedBasesFrom(World world);
}
