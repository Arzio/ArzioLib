package com.arzio.arziolib.module.fix;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.packet.CDCombatlogShowTimer;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleFixCorpseDuplication extends ListenerModule{

	public ModuleFixCorpseDuplication(ArzioLib plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "fix-corpse-item-duplication";
	}
	
	@EventHandler
	public void onCombatLogPacket(CDCombatlogShowTimer event) {
		event.setCancelled(true);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		
		Bukkit.getScheduler().runTaskLater(this.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				getPlugin().getCraftingDeadMain().setTrackerEnabled(false);
			}
			
		}, 1L);
		
	}

	@Override
	public void onDisable() {
		super.onDisable();
		
		Bukkit.getScheduler().runTaskLater(this.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				getPlugin().getCraftingDeadMain().setTrackerEnabled(true);
			}
			
		}, 1L);

	}

}
