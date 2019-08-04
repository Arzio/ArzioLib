package com.arzio.arziolib.module.addon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.BaseProvider;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-respawn-at-base", defaultState = false)
public class ModuleAddonRespawnAtBase extends Module {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent event) {
		BaseProvider provider = ArzioLib.getInstance().getBaseProvider();
		Player player = event.getPlayer();

		if (provider.hasBase(player)) {
			event.setRespawnLocation(provider.getBaseFromPlayer(player).getLocation().clone().add(0.5D, 1D, 0.5D));
		}
	}

}
