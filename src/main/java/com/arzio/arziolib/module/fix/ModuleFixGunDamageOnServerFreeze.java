package com.arzio.arziolib.module.fix;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitTask;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.packet.CDBulletHitEvent;
import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "fix-gun-damage-on-server-freeze")
public class ModuleFixGunDamageOnServerFreeze extends Module{

	private BukkitTask task;
	private long lastTick = 0L;
	private long suspendedMillis = 0L;
	private YMLFile yml;
	
	private int minimumFreezeTime;
	private int bulletSuspensionDuration;
	
	@Override
	public void onEnable() {
		super.onEnable();
		
        this.yml = new YMLFile(this.getPlugin(), "module_configuration/suspend-bullets-on-server-freeze.yml");
		yml.reload();
		
		this.minimumFreezeTime = yml.getValueWithDefault("minimum-freeze-time-in-millis-until-suspend", 700);
		this.bulletSuspensionDuration = yml.getValueWithDefault("bullet-suspension-duration-in-millis", 400);
		
		yml.save();
		
		task = Bukkit.getScheduler().runTaskTimer(ArzioLib.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if (suspendedMillis > 0L) {
					suspendedMillis -= getDifferenceBetweenLastTick();
				}
				
				recalculateSuspendedMillis();
				
				lastTick = System.currentTimeMillis();
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
	
	private long getDifferenceBetweenLastTick() {
		long timeNow = System.currentTimeMillis();
		long timeDifferenceBetweenLastTick = timeNow - lastTick;
		return timeDifferenceBetweenLastTick;
	}
	
	private void recalculateSuspendedMillis() {
		if (getDifferenceBetweenLastTick() > this.minimumFreezeTime) {
			suspendedMillis = this.bulletSuspensionDuration; // Suspends every bullet and gun trigger for some millis
		}
	}
	
	public boolean canFire() {
		this.recalculateSuspendedMillis(); // Bullet packets happens before Bukkit's tick task, so we need to recalculate
		return 0 >= this.suspendedMillis;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void disableGunTriggerOnFreeze(CDGunTriggerEvent event) {
		if (!this.canFire()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void disableBulletHitOnFreeze(CDBulletHitEvent event) {
		if (!this.canFire()) {
			event.setCancelled(true);
		}
	}

}
