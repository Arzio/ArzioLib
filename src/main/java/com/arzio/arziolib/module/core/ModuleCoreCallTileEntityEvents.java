package com.arzio.arziolib.module.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

import com.arzio.arziolib.api.event.TileEntityLoadEvent;
import com.arzio.arziolib.api.event.TileEntityUnloadEvent;
import com.arzio.arziolib.api.util.reflection.ReflectedClass;
import com.arzio.arziolib.api.util.reflection.ReflectedField;
import com.arzio.arziolib.api.util.reflection.finder.ContentFinder;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

import net.minecraft.server.v1_6_R3.TileEntity;
import net.minecraft.server.v1_6_R3.World;

@RegisterModule(name = "core-call-tile-entity-events")
public class ModuleCoreCallTileEntityEvents extends Module{
	
	private ReflectedField<List<?>> nmsWorldTileEntityListField;

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWorldInit(WorldInitEvent event) {
		CraftWorld craftWorld = (CraftWorld) event.getWorld();
		World nmsWorld = craftWorld.getHandle();

		if (nmsWorldTileEntityListField == null){
			nmsWorldTileEntityListField = new ReflectedField<>(World.class, new ContentFinder.FieldBuilder<>().withRegexName("field\\_73009\\_h").build());
		}

		// Swaps the World's entityList ArrayList with my event-based ArrayList :DD
		nmsWorldTileEntityListField.setValue(nmsWorld, new TileEntityEventSet(nmsWorld));
	}
	
	public static class TileEntityEventSet extends ArrayList<TileEntity> {
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
