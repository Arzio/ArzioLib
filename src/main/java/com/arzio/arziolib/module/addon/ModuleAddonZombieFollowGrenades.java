package com.arzio.arziolib.module.addon;

import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.ai.PathfinderGoalNearestGrenade;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.EntityUtil;
import com.arzio.arziolib.module.ListenerModule;

import net.minecraft.server.v1_6_R3.EntityCreature;

public class ModuleAddonZombieFollowGrenades extends ListenerModule{

	public ModuleAddonZombieFollowGrenades(ArzioLib plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		
		for (CDEntityType type : CDEntityType.getZombieTypes()) {
			if (type.isTypeOf(entity)) {
				EntityCreature creature = ((CraftCreature) event.getEntity()).getHandle();
				EntityUtil.getGoalSelector(creature).a(2, new PathfinderGoalNearestGrenade(creature, 1D, false));
				break;
			}
		}
	}

	@Override
	public String getName() {
		return "addon-zombie-follow-grenades";
	}
}
