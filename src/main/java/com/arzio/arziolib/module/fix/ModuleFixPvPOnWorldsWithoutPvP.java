package com.arzio.arziolib.module.fix;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "fix-damage-on-worlds-without-pvp")
public class ModuleFixPvPOnWorldsWithoutPvP extends Module {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAttack(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			if (!event.getEntity().getWorld().getPVP()) {
				event.setCancelled(true);
			}
		}
	}

}
