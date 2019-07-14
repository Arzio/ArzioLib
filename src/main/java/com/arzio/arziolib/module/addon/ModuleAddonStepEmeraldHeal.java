package com.arzio.arziolib.module.addon;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDPotionEffectType;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-step-emerald-heal", defaultState = false)
public class ModuleAddonStepEmeraldHeal extends Module {
	
	private PlayerDataHandler playerDataHandler = ArzioLib.getInstance().getPlayerDataHandler();
	
	@EventHandler(ignoreCancelled = true)
	public void onWalkIntoEmerald(PlayerMoveEvent event) {
		if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() != Material.EMERALD_BLOCK) {
			return;
		}

		Player player = event.getPlayer();
		if (player.hasPotionEffect(CDPotionEffectType.BLEEDING) 
				|| player.hasPotionEffect(CDPotionEffectType.BROKEN_LEG)
				|| player.hasPotionEffect(CDPotionEffectType.INFECTION)) {

			player.removePotionEffect(CDPotionEffectType.BLEEDING);
			player.removePotionEffect(CDPotionEffectType.BROKEN_LEG);
			player.removePotionEffect(CDPotionEffectType.INFECTION);
			event.getPlayer().playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 1.0F, 2.0F);
		}
		event.getPlayer().setFoodLevel(20);
		playerDataHandler.getPlayerData(player).setWaterLevel(20);
	}

}
