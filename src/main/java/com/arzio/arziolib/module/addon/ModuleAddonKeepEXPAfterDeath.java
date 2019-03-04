package com.arzio.arziolib.module.addon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleAddonKeepEXPAfterDeath extends ListenerModule {
	
	public ModuleAddonKeepEXPAfterDeath(ArzioLib plugin, boolean defaultState) {
		super(plugin, defaultState);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onDeath(PlayerDeathEvent event){
		event.setKeepLevel(true);
		event.setDroppedExp(0);
	}

	@Override
	public String getName() {
		return "addon-keep-exp-after-death";
	}

}
