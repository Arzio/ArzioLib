package com.arzio.arziolib.module.addon;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.ItemStackHelper;
import com.arzio.arziolib.api.event.packet.CDGunReloadEvent;
import com.arzio.arziolib.api.util.CDAttachment;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.wrapper.Ammo;
import com.arzio.arziolib.api.wrapper.Gun;
import com.arzio.arziolib.api.wrapper.ItemProvider;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-equip-guns-from-inventory")
public class ModuleAddonEquipGunsFromInventory extends Module {
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event){
		
		ItemStack cursorItem = event.getCursor();
		ItemStack clickedStack = event.getCurrentItem();
		
		ItemStackHelper helper = ArzioLib.getInstance().getItemStackHelper();
		ItemProvider provider = ArzioLib.getInstance().getItemProvider();
		
		if (event.getAction() == InventoryAction.PICKUP_HALF) {
			Gun clickedGun = provider.getStackAs(Gun.class, clickedStack);
			
			if (clickedGun == null) {
				return;
			}
			
			ItemStack currentClip = helper.getGunClip(clickedStack);
			
			if (currentClip != null && currentClip.getType() != Material.AIR) {
				CDGunReloadEvent innerEvent = new CDGunReloadEvent((Player) event.getWhoClicked(), clickedGun, null);
				Bukkit.getPluginManager().callEvent(innerEvent);
				
				if (!innerEvent.isCancelled()) {
					event.setCancelled(true);
					event.setCursor(currentClip);
					
					helper.setGunClip(clickedStack, new ItemStack(Material.AIR));
					event.setCurrentItem(clickedStack); // updates the inventory slot, fixing a CD duplication glitch.
					
					CauldronUtils.playSound(event.getWhoClicked().getLocation(), clickedGun.getReloadSound(), 1F, 1.4F);
				}
			}
			
			return;
		} else if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
			
			Ammo cursorAmmo = provider.getStackAs(Ammo.class, cursorItem);
			Gun clickedGun = provider.getStackAs(Gun.class, clickedStack);
			
			CDAttachment attachment = CDAttachment.getFrom(cursorItem);
			
			if (clickedGun != null && attachment != null) {
				boolean isCompatible = false;
				
				for (CDAttachment compatAttach : clickedGun.getCompatibleAttachments()) {
					if (compatAttach == attachment) {
						isCompatible = true;
						break;
					}
				}
				
				if (!isCompatible) {
					return;
				}
				
				event.setCancelled(true);
				
				CDAttachment oldAttachment = helper.getAttachment(clickedStack, attachment.getType());
				helper.setAttachment(clickedStack, attachment.getType(), attachment);
				event.setCurrentItem(clickedStack);
				event.setCursor(oldAttachment == null ? new ItemStack(Material.AIR) : new ItemStack(oldAttachment.getMaterial().asMaterial()));
				
				Player player = (Player) event.getWhoClicked();
				player.playSound(event.getWhoClicked().getLocation(), Sound.ANVIL_USE, 1F, 1F);
				return;
			}
			
			if (clickedGun != null && cursorAmmo != null) {

				boolean isCompatible = false;
				
				for (Material m : clickedGun.getCompatibleAmmos()) {
					if (m == cursorItem.getType()) {
						isCompatible = true;
						break;
					}
				}
				
				if (!isCompatible) {
					return;
				}

				CDGunReloadEvent innerEvent = new CDGunReloadEvent((Player) event.getWhoClicked(), clickedGun, null);
				Bukkit.getPluginManager().callEvent(innerEvent);
				
				if (!innerEvent.isCancelled()) {
					event.setCancelled(true);
					
					ItemStack currentClip = helper.getGunClip(clickedStack);
					event.setCursor(currentClip != null ? currentClip : new ItemStack(Material.AIR));
					
					helper.setGunClip(clickedStack, cursorItem);
					event.setCurrentItem(clickedStack); // updates the inventory slot, fixing a CD duplication glitch.
					
					CauldronUtils.playSound(event.getWhoClicked().getLocation(), clickedGun.getReloadSound(), 2F, 1F);
				}
				return;
			}
			
		}
	}

}
