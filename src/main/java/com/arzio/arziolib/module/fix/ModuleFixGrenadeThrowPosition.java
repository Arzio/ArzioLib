package com.arzio.arziolib.module.fix;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.EntityJoinWorldEvent;
import com.arzio.arziolib.api.event.packet.CDGrenadeThrowEvent;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleFixGrenadeThrowPosition extends ListenerModule{
	
	private Player lastThrower = null;
	
	public ModuleFixGrenadeThrowPosition(ArzioLib plugin) {
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onThrow(CDGrenadeThrowEvent event) {
		this.lastThrower = event.getPlayer();
	}

	@EventHandler(ignoreCancelled = true)
	public void onGrenadeJoinWorld(EntityJoinWorldEvent event) {
		
		Entity entity = event.getEntity();
		
		if (this.lastThrower == null) {
			return;
		}
		
		for (CDEntityType type : CDEntityType.getGrenadeTypes()) {
			if (type.isTypeOf(entity)) {
				entity.teleport(this.lastThrower.getEyeLocation().subtract(0D, 0.2D, 0D));
			}
		}
		
		this.lastThrower = null;
	}

	@Override
	public String getName() {
		return "fix-grenade-throw-position";
	}

}
