package com.arzio.arziolib.module.addon;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDLootType;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleAddonRespawnLoots extends ListenerModule {

	public ModuleAddonRespawnLoots(ArzioLib plugin, boolean state) {
		super(plugin, state);
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event){
		for (BlockState state : event.getChunk().getTileEntities()) {
			CDLootType lootType = CDLootType.getFrom(state.getBlock());
			if (lootType != null) {
				Location location = state.getLocation();
				location.getBlock().setType(Material.AIR);
				location.getBlock().setType(lootType.getMaterial().asMaterial());
			}
		}
	}

	@Override
	public String getName() {
		return "addon-respawn-loots-on-chunk-load";
	}

}
