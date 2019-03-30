package com.arzio.arziolib.module.addon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.BaseProvider;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleAddonRespawnAtBase extends ListenerModule {
	
	public ModuleAddonRespawnAtBase(ArzioLib plugin, boolean defaultState) {
		super(plugin, defaultState);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent event){
		BaseProvider provider = this.getPlugin().getBaseProvider();
		Player player = event.getPlayer();
		
		if (provider.hasBase(player)) {
			event.setRespawnLocation(provider.getBaseFromPlayer(player).getLocation().clone().add(0.5D, 1D, 0.5D));
		}
	}

	@Override
	public String getName() {
		return "addon-respawn-at-base";
	}

}
