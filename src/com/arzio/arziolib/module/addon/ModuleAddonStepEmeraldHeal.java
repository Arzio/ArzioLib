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
import com.arzio.arziolib.module.ListenerModule;

public class ModuleAddonStepEmeraldHeal extends ListenerModule {
	
	private PlayerDataHandler playerDataHandler;
	
	public ModuleAddonStepEmeraldHeal(ArzioLib plugin, boolean defaultState) {
		super(plugin, defaultState);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onDeath(PlayerMoveEvent event) {
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

	@Override
	public String getName() {
		return "addon-step-emerald-heal";
	}

}
