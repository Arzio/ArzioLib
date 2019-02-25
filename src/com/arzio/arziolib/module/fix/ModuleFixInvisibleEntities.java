package com.arzio.arziolib.module.fix;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.EntityUtil;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleFixInvisibleEntities extends ListenerModule{

	public ModuleFixInvisibleEntities(ArzioLib plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.getTo().getWorld() != event.getFrom().getWorld()) {
			
			// Delete the head before teleporting
			deleteHeadEntity(event.getPlayer());
		} else if (event.getFrom().distance(event.getTo()) < 20.0D) {
			return;
		}
		
		final Player player = event.getPlayer();
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				if (player.isValid()) {
					EntityUtil.refreshEntityTrackers(player);
					
					// Delete the head again, just for being sure it will be showing up in the next ticks
					// The mod will respawn it right after the deletion.
					deleteHeadEntity(player);
				}
			}
		};
		runnable.runTaskLater(ArzioLib.getInstance(), 20L);
	}

	private static void deleteHeadEntity(Player player) {
		List<Entity> list = player.getNearbyEntities(5.0D, 5.0D, 5.0D);
		for (Entity e : list) {
			if (CDEntityType.HEAD.isTypeOf(e)) {
				e.remove();
			}
		}
	}

	@Override
	public String getName() {
		return "fix-invisible-entities";
	}

}
