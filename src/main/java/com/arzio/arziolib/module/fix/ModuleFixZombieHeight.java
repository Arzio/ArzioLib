package com.arzio.arziolib.module.fix;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.EntityUtil;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleFixZombieHeight extends ListenerModule{

	public ModuleFixZombieHeight(ArzioLib plugin) {
		super(plugin);
	}

	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		
		for (CDEntityType type : CDEntityType.getZombieTypes()) {
			if (type.isTypeOf(entity)) {
				EntityUtil.setEntitySize(entity, 0.6F, 1.9F);
				break;
			}
		}
	}

	@Override
	public String getName() {
		return "fix-zombie-height";
	}
}
