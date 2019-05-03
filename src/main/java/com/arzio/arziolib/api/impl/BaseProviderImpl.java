package com.arzio.arziolib.api.impl;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.BaseProvider;
import com.arzio.arziolib.api.util.CDBaseMaterial;
import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.arziolib.api.wrapper.PlayerData;
import com.arzio.arziolib.api.wrapper.impl.BaseImpl;

public class BaseProviderImpl implements BaseProvider{

	private Map<Block, Base> dataInCache = new HashMap<Block, Base>();
	
	@Override
	public Base getBaseFromPlayer(Player player) {
		PlayerData data = ArzioLib.getInstance().getPlayerDataHandler().getPlayerData(player);
		return hasBase(player) ? getBaseFromCenter(data.getBaseLocation()) : null;
	}
	
	@Override
	public Base getBaseFromCenter(BlockState state) {
		return this.getBaseFromCenter(state.getBlock());
	}
	
	@Override
	public Base getBaseFromCenter(Location location) {
		return this.getBaseFromCenter(location.getBlock());
	}
	
	@Override
	public Base getBaseFromCenter(Block block) {
		return this.loadBaseIfNotAlready(block);
	}
	
	private Base loadBaseIfNotAlready(Block block) {
		if (!CDBaseMaterial.isCenter(block)) {
			return null;
		}
		
		Base base = dataInCache.get(block);
		
		if (base == null) {
			base = new BaseImpl(block);
			dataInCache.put(block, base);
		}
		
		return base;
	}

	@Override
	public Base getBaseFromPart(Block block) {
		Set<Base> bases = this.getLoadedBasesFrom(block.getWorld());
		for (Base base : bases) {
			if (base.isPartOfBase(block)) {
				return base;
			}
		}
		return null;
	}

	@Override
	public boolean isPartOfAnyBase(Block block) {
		return this.getBaseFromPart(block) != null;
	}

	@Override
	public boolean hasBase(Player player) {
		PlayerData data = ArzioLib.getInstance().getPlayerDataHandler().getPlayerData(player);
		return data.getBaseLocation() != null;
	}

	@Override
	public Set<Base> getLoadedBasesFrom(World world) {
		clearInvalidBasesFrom(world);
		addUncachedBasesFrom(world);
		
		return getCurrentlyLoadedBases(world);
	}

	protected void removeBase(Base base) {
		dataInCache.remove(base.getLocation().getBlock());
	}
	
	private void addUncachedBasesFrom(World world) {
		for (Chunk chunk : world.getLoadedChunks()) {
			for (BlockState baseCenter : chunk.getTileEntities()) {
				if (CDBaseMaterial.isCenter(baseCenter)) {
					loadBaseIfNotAlready(baseCenter.getBlock());
				}
			}
		}
	}
	
	private void clearInvalidBasesFrom(World world) {
		for (Base base : getCurrentlyLoadedBases(world)) {
			if (!base.getLocation().getChunk().isLoaded() || !base.isValid()) {
				base.destroy();
			}
		}
	}
	
	private Set<Base> getCurrentlyLoadedBases(World world){
		Set<Base> newSet = new LinkedHashSet<Base>();
		
		for (Base b : dataInCache.values()) {
			if (b.getLocation().getWorld().equals(world)) {
				newSet.add(b);
			}
		}
		
		return newSet;
	}

}
