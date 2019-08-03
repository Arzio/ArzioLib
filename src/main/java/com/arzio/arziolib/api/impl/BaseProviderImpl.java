package com.arzio.arziolib.api.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.BaseProvider;
import com.arzio.arziolib.api.event.TileEntityLoadEvent;
import com.arzio.arziolib.api.event.TileEntityUnloadEvent;
import com.arzio.arziolib.api.util.CDBaseMaterial;
import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;
import com.arzio.arziolib.api.wrapper.impl.BaseImpl;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class BaseProviderImpl implements BaseProvider, Listener{

	private Table<World, Block, Base> dataInCache = HashBasedTable.create();
	private final PlayerDataHandler playerDataHandler = ArzioLib.getInstance().getPlayerDataHandler();
	
	public BaseProviderImpl(){
		// Adds every uncached bases to the cache
		for (World world : Bukkit.getWorlds()){
			this.addUncachedBasesFrom(world);
		}

		Bukkit.getPluginManager().registerEvents(this, ArzioLib.getInstance());
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event){
		addUncachedBasesFrom(event.getWorld());
	}

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event){
		addUncachedBasesFrom(event.getChunk());
	}

	@EventHandler
	public void onWorldUnload(WorldUnloadEvent event){
		clearBasesFrom(event.getWorld());
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event){
		clearBasesFrom(event.getChunk());
	}

	@EventHandler
	public void onTileEntityLoad(TileEntityLoadEvent event){
		if (CDBaseMaterial.isCenter(event.getBlock())){
			getBaseFromCenter(event.getBlock());
		}
	}

	@EventHandler
	public void onTileEntityUnload(TileEntityUnloadEvent event){
		if (CDBaseMaterial.isCenter(event.getBlock())){
			this.removeBase(this.getBaseFromCenter(event.getBlock()));
		}
	}

	@Override
	public Base getBaseFromPlayer(Player player) {
		return playerDataHandler.getPlayerData(player).getBase();
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
		return this.getBaseFromBlock(block);
	}
	
	private Base getBaseFromBlock(Block block) {
		if (!CDBaseMaterial.isCenter(block)) {
			return null;
		}
		
		Base base = dataInCache.get(block.getWorld(), block);
		
		if (base == null) {
			base = new BaseImpl(block);
			dataInCache.put(block.getWorld(), block, base);
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
		return playerDataHandler.getPlayerData(player).hasBase();
	}

	@Override
	public Set<Base> getLoadedBasesFrom(World world) {
		Set<Base> bases = new HashSet<>(this.dataInCache.rowMap().get(world).values());
		return Collections.unmodifiableSet(bases);
	}

	protected void removeBase(Base base) {
		dataInCache.remove(base.getBlock().getWorld(), base.getLocation().getBlock());
	}

	private void addUncachedBasesFrom(World world) {
		for (Chunk chunk : world.getLoadedChunks()) {
			this.addUncachedBasesFrom(chunk);
		}
	}
	
	private void addUncachedBasesFrom(Chunk chunk) {
		for (BlockState baseCenter : chunk.getTileEntities()) {
			if (CDBaseMaterial.isCenter(baseCenter)) {
				getBaseFromBlock(baseCenter.getBlock());
			}
		}
	}

	private void clearBasesFrom(World world){
		for (Chunk chunk : world.getLoadedChunks()){
			clearBasesFrom(chunk);
		}
	}
	
	private void clearBasesFrom(Chunk chunk) {
		for (BlockState baseCenter : chunk.getTileEntities()) {
			if (CDBaseMaterial.isCenter(baseCenter)) {
				this.dataInCache.remove(chunk.getWorld(), baseCenter.getBlock());
			}
		}
	}

}
