package com.arzio.arziolib.module.addon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-keep-exp-after-death", defaultState = false)
public class ModuleAddonKeepEXPAfterDeath extends Module {
	
	@EventHandler(ignoreCancelled = true)
	public void onDeath(PlayerDeathEvent event){
		event.setKeepLevel(true);
		event.setDroppedExp(0);
	}

}
