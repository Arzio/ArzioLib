package com.arzio.arziolib.module.fix;

import org.bukkit.event.EventHandler;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.packet.CDSwapGunEvent;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleFixSwapGunFastShoot extends ListenerModule {

	public ModuleFixSwapGunFastShoot(ArzioLib plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void removeGunSwap(CDSwapGunEvent event) {
		event.setCancelled(true);
		event.getPlayer().sendMessage("§cThis button is disabled due to fast-shooting bug abuse.");
	}

	@Override
	public String getName() {
		return "fix-gun-swap-fast-shoot";
	}

}
