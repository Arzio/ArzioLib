package com.arzio.arziolib.api.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.minecraft.server.v1_6_R3.TileEntity;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.BaseProvider;
import com.arzio.arziolib.api.util.CDBaseMaterial;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;
import com.arzio.arziolib.api.wrapper.impl.BaseImpl;

public class BaseProviderImpl implements BaseProvider, Listener{

	private final PlayerDataHandler playerDataHandler = ArzioLib.getInstance().getPlayerDataHandler();
	
	public BaseProviderImpl(){
		Bukkit.getPluginManager().registerEvents(this, ArzioLib.getInstance());
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
		
		return new BaseImpl(block);
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
		CraftWorld craftWorld = (CraftWorld) world;
		net.minecraft.server.v1_6_R3.WorldServer nmsWorld = craftWorld.getHandle();

		Set<Base> bases = new LinkedHashSet<>();

		for (net.minecraft.server.v1_6_R3.Chunk nmsChunk : nmsWorld.chunkProviderServer.chunks.values()){
			for (Object obj : nmsChunk.tileEntities.values()){

				// Checks if the tile entity is a Base Center
				if (obj.getClass() == CDClasses.tileEntityBaseCenterClass.getReferencedClass()){
					TileEntity tileBase = (TileEntity) obj;
					bases.add(this.getBaseFromCenter(world.getBlockAt(tileBase.x, tileBase.y, tileBase.z)));
				}
			}
		}
		
		return Collections.unmodifiableSet(bases);
	}
}
