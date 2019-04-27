package com.arzio.arziolib.module.fix;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.EntityUtil;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleFixInvisibleEntities extends ListenerModule{

	public ModuleFixInvisibleEntities(ArzioLib plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		// If the distance of teleport is less than 20, simply do nothing.
		if (event.getTo().getWorld() == event.getFrom().getWorld() && event.getFrom().distance(event.getTo()) < 20.0D) {
			return;
		}
		
		final Player player = event.getPlayer();
		
		// Refresh all the trackers for this player
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				if (player.isValid()) {
					EntityUtil.refreshEntityTrackers(player);
				}
			}
		};
		runnable.runTaskLater(ArzioLib.getInstance(), 20L);
	}

	@Override
	public String getName() {
		return "fix-invisible-entities";
	}

}
