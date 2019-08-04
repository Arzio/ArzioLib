package com.arzio.arziolib.module.fix.deprecated;

import org.bukkit.event.EventHandler;

import com.arzio.arziolib.api.event.packet.CDSwitchSlotEvent;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@Deprecated
@RegisterModule(name = "fix-gun-swap-fast-shoot")
public class ModuleFixSwapGunFastShoot extends Module {

	@EventHandler
	public void removeGunSwap(CDSwitchSlotEvent event) {
		event.setCancelled(true);
		event.getPlayer().sendMessage("§cThis button is disabled due to fast-shooting bug abuse.");
	}

}
