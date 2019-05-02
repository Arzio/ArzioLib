package com.arzio.arziolib.module.fix;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.EntityJoinWorldEvent;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.EntityUtil;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleFixZombieHeight extends ListenerModule{

	public ModuleFixZombieHeight(ArzioLib plugin) {
		super(plugin);
	}

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

	@Override
	public String getName() {
		return "fix-zombie-height";
	}
}
