package com.arzio.arziolib.module.core;

import org.bukkit.Bukkit;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.reflection.ReflectionHelper;
import com.arzio.arziolib.module.ListenerModule;

import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class ModuleCoreCallEntityJoinWorldEvent extends ListenerModule{

	public ModuleCoreCallEntityJoinWorldEvent(ArzioLib plugin) {
		super(plugin);
	}
	
	@ForgeSubscribe
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = ReflectionHelper.getValueFromEvent(event, Entity.class);
		World world = ReflectionHelper.getValueFromEvent(event, World.class);
		
		com.arzio.arziolib.api.event.EntityJoinWorldEvent innerEvent = new com.arzio.arziolib.api.event.EntityJoinWorldEvent(entity.getBukkitEntity(), world.getWorld());
		
		Bukkit.getPluginManager().callEvent(innerEvent);
		
		if (innerEvent.isCancelled()) {
			event.setCanceled(true);
		}
	}

	@Override
	public String getName() {
		return "core-call-entity-join-world-event";
	}

}
