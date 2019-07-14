package com.arzio.arziolib.module.core;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

import com.arzio.arziolib.api.event.EntityJoinWorldEvent;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.World;

@RegisterModule(name = "core-call-entity-join-world-event")
public class ModuleCoreCallEntityJoinWorldEvent extends Module{
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onWorldInit(WorldInitEvent event) {
		CraftWorld craftWorld = (CraftWorld) event.getWorld();
		World nmsWorld = craftWorld.getHandle();
		
		// Swaps the World's entityList ArrayList with my event-based ArrayList :DD
		nmsWorld.entityList = new EntityEventArrayList(nmsWorld);
	}
	
	public static class EntityEventArrayList extends ArrayList<Entity> {
		private static final long serialVersionUID = 6551028671379463300L;
		
		private final World nmsWorld;
		
		public EntityEventArrayList(World nmsWorld) {
			this.nmsWorld = nmsWorld;
		}
		
		@Override
		public boolean add(Entity e) {
			EntityJoinWorldEvent innerEvent = new EntityJoinWorldEvent(e.getBukkitEntity(), nmsWorld.getWorld());
			Bukkit.getPluginManager().callEvent(innerEvent);
			
			if (innerEvent.isCancelled()) {
				e.die();
			}
			
			return super.add(e);
		}
	}

}
