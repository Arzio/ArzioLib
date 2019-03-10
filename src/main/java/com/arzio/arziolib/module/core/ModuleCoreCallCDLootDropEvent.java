package com.arzio.arziolib.module.core;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.CDLootDropEvent;
import com.arzio.arziolib.api.event.EntityJoinWorldEvent;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.CDLootType;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleCoreCallCDLootDropEvent extends ListenerModule{

	private boolean willLootItemDrop = false;
	
	public ModuleCoreCallCDLootDropEvent(ArzioLib plugin) {
		super(plugin);
	}
	
	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (willLootItemDrop) {
			willLootItemDrop = false;
			
			if (CDEntityType.GROUND_ITEM.isTypeOf(event.getEntity())) {
				CDLootDropEvent innerEvent = new CDLootDropEvent(event.getEntity());
				Bukkit.getPluginManager().callEvent(innerEvent);
				
				if (innerEvent.isCancelled()) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onConsumeLoot(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if (CDLootType.getFrom(event.getClickedBlock()) != null) {
				this.willLootItemDrop = true;
			}
		}
	}
	

	@Override
	public String getName() {
		return "core-call-loot-drop-event";
	}

}
