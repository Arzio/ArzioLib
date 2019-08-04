package com.arzio.arziolib.module.fix.deprecated;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;

import com.arzio.arziolib.api.event.EntityJoinWorldEvent;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.EntityUtil;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@Deprecated
@RegisterModule(name = "fix-zombie-height")
public class ModuleFixZombieHeight extends Module {

	@EventHandler
	public void onZombieJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();

		for (CDEntityType type : CDEntityType.getZombieTypes()) {
			if (type.isTypeOf(entity)) {
				EntityUtil.setEntitySize(entity, 0.8F, 1.9F);
				break;
			}
		}
	}
}
