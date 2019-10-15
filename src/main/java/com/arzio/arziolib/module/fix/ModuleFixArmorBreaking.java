package com.arzio.arziolib.module.fix;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.api.event.packet.CDBulletHitEvent;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

/**
 * Fixes the hidden armor durability when receiving bullet damage.
 * 
 * @param event
 */

@RegisterModule(name = "fix-armor-breaking")
public class ModuleFixArmorBreaking extends Module {

	@EventHandler
	public void armorBreakingFix(CDBulletHitEvent event) {
		if (!(event.getEntityHit() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getEntityHit();

		ItemStack stackHelmet = player.getEquipment().getHelmet();
		if (stackHelmet != null) {
			stackHelmet.setDurability((short) 1);
			player.getEquipment().setHelmet(stackHelmet);
		}
		ItemStack stackArmor = player.getEquipment().getChestplate();
		if (stackArmor != null) {
			stackArmor.setDurability((short) 1);
			player.getEquipment().setChestplate(stackArmor);
		}
		ItemStack stackLegs = player.getEquipment().getLeggings();
		if (stackLegs != null) {
			stackLegs.setDurability((short) 1);
			player.getEquipment().setLeggings(stackLegs);
		}
	}
}
