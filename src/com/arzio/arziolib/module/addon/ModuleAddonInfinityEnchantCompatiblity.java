package com.arzio.arziolib.module.addon;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleAddonInfinityEnchantCompatiblity extends ListenerModule {
	
	public ModuleAddonInfinityEnchantCompatiblity(ArzioLib plugin) {
		super(plugin);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onFire(CDGunTriggerEvent event){
		if (event.getPlayer().getItemInHand().containsEnchantment(Enchantment.ARROW_INFINITE)) {
			event.setSpendAmmo(false);
		}
	}

	@Override
	public String getName() {
		return "addon-infinity-enchantment-compatiblity";
	}

}
