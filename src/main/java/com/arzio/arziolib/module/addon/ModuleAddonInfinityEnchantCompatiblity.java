package com.arzio.arziolib.module.addon;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-infinity-enchantment-compatiblity")
public class ModuleAddonInfinityEnchantCompatiblity extends Module {
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onFire(CDGunTriggerEvent event){
		if (event.getPlayer().getItemInHand().containsEnchantment(Enchantment.ARROW_INFINITE)) {
			event.setSpendAmmo(false);
		}
	}

}
