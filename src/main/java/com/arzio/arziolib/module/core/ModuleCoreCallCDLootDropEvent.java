package com.arzio.arziolib.module.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.arzio.arziolib.api.event.CDLootDropEvent;
import com.arzio.arziolib.api.event.EntityJoinWorldEvent;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.CDLootType;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "core-call-loot-drop-event")
public class ModuleCoreCallCDLootDropEvent extends Module{

	private Player lastPlayer = null;
	private boolean willLootItemDrop = false;
	
	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (willLootItemDrop) {
			
			if (CDEntityType.GROUND_ITEM.isTypeOf(event.getEntity())) {
				CDLootDropEvent innerEvent = new CDLootDropEvent(this.lastPlayer, event.getEntity());
				Bukkit.getPluginManager().callEvent(innerEvent);
				
				if (innerEvent.isCancelled()) {
					event.setCancelled(true);
				}
			}
			
			willLootItemDrop = false;
			lastPlayer = null;
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onConsumeLoot(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if (CDLootType.getFrom(event.getClickedBlock()) != null) {
				this.willLootItemDrop = true;
				this.lastPlayer = event.getPlayer();
			}
		}
	}

}
