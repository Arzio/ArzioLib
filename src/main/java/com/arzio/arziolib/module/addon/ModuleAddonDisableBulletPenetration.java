package com.arzio.arziolib.module.addon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.packet.CDBulletHitEvent;
import com.arzio.arziolib.api.wrapper.Ammo;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleAddonDisableBulletPenetration extends ListenerModule {
	
	public ModuleAddonDisableBulletPenetration(ArzioLib plugin) {
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBulletHit(CDBulletHitEvent event){
		ItemStack gunStack = this.getPlugin().getItemStackHelper().getGunClip(event.getPlayer().getItemInHand());
		Ammo ammo = this.getPlugin().getItemProvider().getStackAs(Ammo.class, gunStack);
		
		if (ammo != null) {
			ammo.setPenetration(1D);
		}
	}

	@Override
	public String getName() {
		return "addon-disable-bullet-penetration";
	}

}
