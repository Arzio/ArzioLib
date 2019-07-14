package com.arzio.arziolib.module.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.BaseProvider;
import com.arzio.arziolib.api.event.packet.CDRequestBaseDestroyEvent;
import com.arzio.arziolib.api.wrapper.Base;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "core-bukkit-events-for-bases")
public class ModuleCoreBukkitEventsForBases extends Module{

    private BaseProvider baseProvider = ArzioLib.getInstance().getBaseProvider();
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBaseDestroyRequest(CDRequestBaseDestroyEvent event) {
		// We cancel the event and force the use of Base.destroy() method.
		// This method will call the BaseDestroy event before destroying the base.
		
		event.setCancelled(true);
		event.getBase().destroy();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCenterBreak(BlockBreakEvent event) {
		Base base = baseProvider.getBaseFromCenter(event.getBlock());
		
		if (base != null) {
			base.destroy();
		}
	}

}
