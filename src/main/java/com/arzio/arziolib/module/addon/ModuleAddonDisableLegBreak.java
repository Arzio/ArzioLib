package com.arzio.arziolib.module.addon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import com.arzio.arziolib.api.util.CDPotionEffectType;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-disable-leg-break", defaultState = false)
public class ModuleAddonDisableLegBreak extends Module {

	private final PotionEffectType legBreakPotion = CDPotionEffectType.BROKEN_LEG;

	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event){
		Player player = event.getPlayer();

		if (player.hasPotionEffect(legBreakPotion)){
			player.removePotionEffect(legBreakPotion);
			player.removePotionEffect(PotionEffectType.BLINDNESS);
		}
	}

	@EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
	public void onFallDamage(EntityDamageEvent event){
		if (event.getCause() == DamageCause.FALL && event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();

			if (player.hasPotionEffect(legBreakPotion)){
				player.removePotionEffect(legBreakPotion);
				player.removePotionEffect(PotionEffectType.BLINDNESS);
			}
		}
	}

}
