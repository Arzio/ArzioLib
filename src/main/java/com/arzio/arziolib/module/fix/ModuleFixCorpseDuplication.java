package com.arzio.arziolib.module.fix;

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
		this.getPlugin().getCraftingDeadMain().setTrackerEnabled(false);
	}

	@Override
	public void onDisable() {
		super.onDisable();
		this.getPlugin().getCraftingDeadMain().setTrackerEnabled(true);
	}

}
