package com.arzio.arziolib.module.fix;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.module.ListenerModule;
import com.worldcretornica.plotme.PlotManager;

import fr.xephi.authme.api.API;

public class ModuleFixPlotMeEntityInteraction extends ListenerModule {

	public ModuleFixPlotMeEntityInteraction(ArzioLib plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "fix-plotme-entity-interaction";
	}

	@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
	public void fixInteraction(PlayerInteractEntityEvent event) {
		if (API.isAuthenticated(event.getPlayer())) {
			if (PlotManager.isPlotWorld(event.getPlayer())) {
				Entity clickedEntity = event.getRightClicked();
				
				if (CDEntityType.GROUND_ITEM.isTypeOf(clickedEntity)
						|| CDEntityType.C4.isTypeOf(clickedEntity)
						|| CDEntityType.CORPSE.isTypeOf(clickedEntity)) {
					event.setCancelled(false);
				}
			}
		}
	}

}
