package com.arzio.arziolib.module.fix;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitTask;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.packet.CDBulletHitEvent;
import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleFixGunDamageOnServerFreeze extends ListenerModule{

	private BukkitTask task;
	private long lastTick = -1;
	private long suspendedMillis = 0L;
	
	public ModuleFixGunDamageOnServerFreeze(ArzioLib plugin) {
		super(plugin);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		task = Bukkit.getScheduler().runTaskTimer(ArzioLib.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				recalculateFreezeStatus();
				
				long timeNow = System.currentTimeMillis();
				long differenceBetweenLastTick = timeNow - lastTick;
				
				if (suspendedMillis > 0L) {
					suspendedMillis -= differenceBetweenLastTick;
				}
				lastTick = timeNow;
			}
		}, 1L, 1L);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		if (task != null) {
			task.cancel();
		}
	}
	
	private void recalculateFreezeStatus() {
		long timeNow = System.currentTimeMillis();
		long differenceBetweenLastTick = timeNow - lastTick;
		
		if (differenceBetweenLastTick > 700L) {
			suspendedMillis = 400L; // Suspends every bullet and gun trigger for more 400 ms
		}
	}
	
	private boolean isAbleToFire() {
		this.recalculateFreezeStatus();
		return this.suspendedMillis <= 0;
	}
	
	/**
	 * Fixes the hidden armor durability when receiving bullet damage.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void disableGunTriggerOnFreeze(CDGunTriggerEvent event) {
		if (!this.isAbleToFire()) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Fixes the hidden armor durability when receiving bullet damage.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void disableBulletHitOnFreeze(CDBulletHitEvent event) {
		if (!this.isAbleToFire()) {
			event.setCancelled(true);
		}
	}

	@Override
	public String getName() {
		return "fix-gun-damage-on-server-freeze";
	}

}
