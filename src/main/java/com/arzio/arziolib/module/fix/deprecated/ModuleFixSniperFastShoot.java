package com.arzio.arziolib.module.fix.deprecated;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.arzio.arziolib.api.event.packet.CDBulletHitEvent;
import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.api.wrapper.Gun;
import com.arzio.arziolib.config.UserData;
import com.arzio.arziolib.config.UserDataProvider;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@Deprecated
@RegisterModule(name = "fix-sniper-fast-shoot")
public class ModuleFixSniperFastShoot extends Module{

	private static final int[] FAST_SHOT_GUNS = new int[] { 9287, 9290 };

	@EventHandler(priority = EventPriority.LOW)
	public void bulletHitForFastShotFix(CDBulletHitEvent event) {
		if (this.isAbusingWithSniper(event.getPlayer(), event.getHeldGun(), true)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void gunTriggerForFastShotFix(CDGunTriggerEvent event) {
		if (this.isAbusingWithSniper(event.getPlayer(), event.getHeldGun(), false)) {
			event.setCancelled(true);
		}
	}
	
	private boolean isAbusingWithSniper(Player player, Gun gun, boolean set) {
		
		if (this.isFastshotAbleWith(gun)) {
			
			UserData userData = UserDataProvider.getInstance().getUserData(player);
			long currentTime = System.currentTimeMillis();
			
			long nextTime = userData.getNextCooldownTimestamp(gun);
			
			if (set) {
				// We reduce the delay in a ratio of 0.85 due to ping delay
				long nextAbleTime = currentTime + (long) (gun.getMillisPerRound() * 0.85D);
				userData.setNextCooldownTimestamp(gun, nextAbleTime);
			}
			
			if (nextTime > currentTime) {
				return true;
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private boolean isFastshotAbleWith(Gun gun) {
		for (int blockedId : FAST_SHOT_GUNS) {
			if (gun.getItem().getId() == blockedId) {
				return true;
			}
		}
		return false;
	}
}
