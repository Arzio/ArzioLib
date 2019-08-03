package com.arzio.arziolib.module.core;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

import com.arzio.arziolib.api.event.TileEntityLoadEvent;
import com.arzio.arziolib.api.event.TileEntityUnloadEvent;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

import net.minecraft.server.v1_6_R3.TileEntity;
import net.minecraft.server.v1_6_R3.World;

@RegisterModule(name = "core-call-tile-entity-events")
public class ModuleCoreCallTileEntityEvents extends Module{
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onWorldInit(WorldInitEvent event) {
		CraftWorld craftWorld = (CraftWorld) event.getWorld();
		World nmsWorld = craftWorld.getHandle();
		
		// Swaps the World's entityList ArrayList with my event-based ArrayList :DD
		nmsWorld.tileEntityList = new TileEntityEventSet(nmsWorld);
	}
	
	public static class TileEntityEventSet extends HashSet<TileEntity> {
		private static final long serialVersionUID = 6551028671379463300L;
		
		private final World nmsWorld;
		
		public TileEntityEventSet(World nmsWorld) {
			this.nmsWorld = nmsWorld;
		}
		
		@Override
		public boolean add(TileEntity tile) {
			org.bukkit.World world = nmsWorld.getWorld();

			TileEntityLoadEvent innerEvent = new TileEntityLoadEvent(world.getBlockAt(tile.x, tile.y, tile.z), tile, world);
			Bukkit.getPluginManager().callEvent(innerEvent);
			
			return super.add(tile);
		}

		@Override
		public boolean remove(Object o) {

			if (o instanceof TileEntity){
				TileEntity tile = (TileEntity) o;
				org.bukkit.World world = nmsWorld.getWorld();

				TileEntityUnloadEvent innerEvent = new TileEntityUnloadEvent(world.getBlockAt(tile.x, tile.y, tile.z), tile, world);
				Bukkit.getPluginManager().callEvent(innerEvent);
			}
			
			return super.remove(o);
		}
	}

}
