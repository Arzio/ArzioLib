package com.arzio.arziolib.module.fix.deprecated;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@Deprecated
@RegisterModule(name = "fix-golden-apple-duplication")
public class ModuleFixGoldenAppleDuplication extends Module{

	/**
	 * Removes the drop of golden apple in order to fix the duplication bug.
	 * @param event
	 */
	@EventHandler
	public void goldenAppleDuplicationFix(PlayerDropItemEvent event) {
		ItemStack drop = event.getItemDrop().getItemStack();

		if (drop.getType() == Material.GOLDEN_APPLE && drop.getData().getData() == (byte) 0) {
			event.getPlayer().sendMessage("§cYou can't drop common golden apples. This can be used in item duplication.");
			event.setCancelled(true);
		}
	}
}
