package com.arzio.arziolib.module.fix;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.EntityJoinWorldEvent;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.EntityUtil;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleFixGrenadeThrowPosition extends ListenerModule{
	
	public ModuleFixGrenadeThrowPosition(ArzioLib plugin) {
		super(plugin);
	}

	@EventHandler
	public void onGrenadeJoinWorld(EntityJoinWorldEvent event) {
		
		Entity entity = event.getEntity();
		
		for (CDEntityType type : CDEntityType.getGrenadeTypes()) {
			if (type.isTypeOf(entity)) {
				Player player = EntityUtil.getClosestPlayerInRadius(entity, 3D);
				
				entity.teleport(player.getEyeLocation().subtract(0D, 0.2D, 0D));
				entity.setVelocity(entity.getVelocity().multiply(1.5F));
			}
		}
	}

	@Override
	public String getName() {
		return "fix-grenade-throw-position";
	}

}
