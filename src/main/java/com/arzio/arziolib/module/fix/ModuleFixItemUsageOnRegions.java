package com.arzio.arziolib.module.fix;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.packet.CDGrenadeThrowEvent;
import com.arzio.arziolib.api.region.Flags;
import com.arzio.arziolib.api.util.CDMaterial;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleFixItemUsageOnRegions extends ListenerModule {

	public ModuleFixItemUsageOnRegions(ArzioLib plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onGrenadeThrowing(CDGrenadeThrowEvent event) {
		if (!Flags.canRegionHavePvP(event.getPlayer().getLocation())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cPVP is disabled in this area!");
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		// Continue only if the clicked entity is a player
		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}
		
		ItemStack stack = event.getPlayer().getItemInHand();
		
		// Check if the clicker is holding any item
		if (stack == null) {
			return;
		}
		
		// First, checks if its one of the disabled items.
		// Only disable the pvp if it is disabled in any of both locations.
		if (stack.getType() == CDMaterial.BLOODBAG.asMaterial() || stack.getType() == CDMaterial.HANDCUFFS.asMaterial()) {
			if (!Flags.canRegionHavePvP(event.getPlayer().getLocation()) || !Flags.canRegionHavePvP(event.getRightClicked().getLocation())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cPVP is disabled here!");
			}
		}
	}

	@Override
	public String getName() {
		return "fix-item-usage-on-regions-without-pvp";
	}

}
